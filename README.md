# MetaMapa

**Sistema de gestión de mapeos colaborativos** · Trabajo Práctico Anual 2025 · Diseño de Sistemas de Información (UTN FRBA)

MetaMapa es una plataforma de código abierto para **recopilar, visibilizar y mapear información de forma colaborativa**. Cada pieza de información es un **hecho** (por ejemplo un foco de incendio, un episodio de contaminación o un desastre natural) con título, descripción, categoría, ubicación, fecha y origen. Los hechos se ven en un mapa interactivo y se agrupan en **colecciones** públicas.

La idea de dominio: la inteligencia colectiva sirve si la información está ordenada, etiquetada y ubicada en tiempo y espacio. Por eso MetaMapa integra **varias fuentes de datos** (datasets, aportes de personas y APIs de terceros), las normaliza, calcula **consenso** entre fuentes y permite navegar en modo *curado* (solo hechos consensuados) o *irrestricto*.

> Pensado para que ONGs, universidades u organismos puedan instalarlo en sus propios servidores como una **instancia** propia, y para que distintas instancias se integren entre sí.

---

## Qué se puede hacer

**Visualizadores (sin login)**
- Explorar hechos y colecciones en un mapa interactivo (Leaflet) con detalle al hacer clic en cada marcador.
- Filtrar por fecha, ubicación, categoría y fuente.
- Alternar entre navegación **curada** e **irrestricta**.
- Ver multimedia de cada hecho y generar **solicitudes de eliminación** con una justificación de al menos 500 caracteres.

**Contribuyentes (anónimos o registrados)**
- Subir hechos con texto, imágenes y otros archivos multimedia.
- Los registrados pueden ver sus hechos y pedir ediciones durante **7 días** desde la carga.

**Administradores**
- Crear y gestionar colecciones, sus fuentes, criterios de pertenencia y algoritmo de consenso.
- Importar datasets CSV como nuevas fuentes estáticas.
- Aceptar, aceptar con cambios o rechazar hechos subidos, pedidos de edición y solicitudes de eliminación.
- Consultar el panel de estadísticas.

---

## Arquitectura

MetaMapa es un **sistema de microservicios** sobre Spring Boot. El cliente web no tiene lógica de negocio: renderiza vistas del lado del servidor y consume únicamente al **API Gateway**, que valida la identidad y enruta cada request al servicio que corresponde.

<img width="6144" height="4224" alt="ARQUIT" src="https://github.com/user-attachments/assets/80967de8-4f14-47d2-9bf5-573d4b40fb42" />



### Módulos

| Módulo | Puerto | Responsabilidad |
|---|---|---|
| `frontend` | 8085 | Cliente liviano con server side rendering (Spring MVC + Thymeleaf). Landing, mapa, colecciones, carga de hechos y panel de administración. |
| `backend/api-gateway` | 8086 | Punto de entrada único. Enruta con Spring Cloud Gateway, valida el JWT y controla roles por ruta. |
| `backend/server-registry` | 8761 | Servidor de descubrimiento Eureka. Los servicios se registran y el gateway los resuelve por nombre. |
| `backend/user-service` | 8087 | Registro, login y refresh de tokens. Emite JWT firmados con RS256. |
| `backend/aggregation-service` | 8080 | Núcleo del sistema: integra las fuentes, normaliza ubicaciones, administra colecciones, consenso y solicitudes de eliminación. |
| `backend/static-source` | 8083 | Fuente estática: importa datasets CSV y expone sus hechos. |
| `backend/dynamic-source` | 8081 | Fuente dinámica: recibe hechos de contribuyentes (con multimedia), revisiones de administradores y pedidos de edición. |
| `backend/proxy-source` | 8082 | Fuente proxy: integra APIs externas (la API de desastres naturales de la cátedra y otras instancias de MetaMapa). |
| `backend/statistics-service` | 8084 | Calcula estadísticas periódicamente a partir de los hechos del agregador. |

Cada servicio con persistencia tiene **su propia base de datos MySQL** (`metamapa_aggregator`, `metamapa_static`, `metamapa_dynamic`, `metamapa_statistics`, `metamapa_users`), así que ningún servicio lee tablas de otro.

---

## Cómo funciona

### Fuentes de datos
Hay tres tipos de fuente, todas detrás del mismo contrato para que el agregador las trate de forma homogénea:

- **Estática** (`static-source`): el administrador sube un CSV. Un `CsvImporter` (OpenCSV) detrás de la interfaz `IImporter` lo parsea, y un scheduler procesa las importaciones pendientes.
- **Dinámica** (`dynamic-source`): los contribuyentes cargan hechos con archivos multimedia. Los archivos se guardan en un almacenamiento compatible con **S3** (MinIO en desarrollo) con límites de cantidad y tamaño y una lista blanca de tipos MIME. Cada carga genera una `SubmissionRequest` que un administrador acepta, acepta con sugerencias o rechaza. Los usuarios registrados pueden pedir ediciones (`EventEditionRequest`) dentro de los 7 días posteriores a la carga.
- **Proxy** (`proxy-source`): encapsula integraciones externas detrás de la interfaz `IAPI` (`NaturalDisastersAPI`, `MetaMapa`). Un scheduler consulta periódicamente las APIs y trae los hechos nuevos usando la fecha de última actualización.

### Patrón Observer entre fuentes y agregador
Las fuentes estática, dinámica y proxy tienen un `Publisher` y un `ISubscriber`. El agregador se registra como suscriptor (`/subscribers`) y recibe los hechos nuevos o modificados apenas se producen. El envío es asincrónico (`WebClient`), por lo que si el agregador no responde la fuente no se bloquea.

### Servicio de agregación
`aggregation-service` guarda una `Source` por cada fuente conectada y un `SourceClient` que sabe cómo hablar con el módulo de origen. Sobre esos hechos resuelve:

- **Normalización**: antes de persistir, `NormalizationService` completa provincia, departamento y municipio a partir de las coordenadas consultando la API **Georef** del Estado argentino. También normaliza categorías para evitar duplicados por variaciones de nombre.
- **Colecciones**: una colección es un conjunto de fuentes más **criterios** (`Condition`: por categoría, desde fecha, hasta fecha, título). Se refrescan con un cron configurable (por defecto, cada hora).
- **Consenso** (patrón Strategy, `IConsensusAlgorithm`): *múltiples menciones*, *mayoría simple* y *absoluta*. Un hecho es equivalente a otro si coinciden título, descripción, coordenadas y fecha. Como puede ser costoso, `ConsensusManager` se reevalúa en un horario de baja carga (por defecto, 3 AM) y no en cada alta.
- **Solicitudes de eliminación**: un `SpamDetector` compara los argumentos (sin *stopwords*) con los de otras solicitudes del mismo hecho y rechaza automáticamente las que son spam. Al aprobar una solicitud, el hecho se marca como eliminado y se avisa a la fuente de origen cuando corresponde.
- **URLs de multimedia**: una estrategia de generación de URL (`ApiGatewayUrlStrategy`) arma links de archivos que pasan por el gateway.

### Servicio de estadísticas
`statistics-service` mantiene su propia base y, cada cierto intervalo (cron configurable), pide al agregador los hechos actualizados. Expone, entre otras:
- provincias con más hechos de una colección o de una categoría,
- categorías con más hechos reportados,
- franjas horarias con más hechos de una categoría.

### API Gateway y seguridad
Todas las rutas se declaran en [`routes.yml`](backend/api-gateway/src/main/resources/routes.yml) con tres piezas:
1. **Balanceo por nombre de servicio** (`lb://AGGREGATION-SERVICE`), resuelto por Eureka.
2. **`StripPrefix=2`**: el gateway publica `/api/v1/...` y los servicios internos exponen rutas sin ese prefijo.
3. **`AuthenticationFilter`** (filtro propio): valida el JWT (RS256), comprueba roles y es configurable por ruta.

```yaml
# ruta pública: cualquiera puede leer colecciones
- id: get-collections
  uri: lb://AGGREGATION-SERVICE
  predicates: [ Path=/api/v1/collections, Method=GET ]
  filters: [ StripPrefix=2 ]

# ruta de administración: exige rol ADMIN
- id: post-collection
  uri: lb://AGGREGATION-SERVICE
  predicates: [ Path=/api/v1/collections, Method=POST ]
  filters: [ StripPrefix=2, AuthenticationFilter=ADMIN ]

# autenticación opcional: anónimo o contribuyente
- id: post-event
  uri: lb://DYNAMIC-SOURCE
  predicates: [ Path=/api/v1/events, Method=POST ]
  filters:
    - StripPrefix=2
    - name: AuthenticationFilter
      args: { required: false, roles: [ADMIN, CONTRIBUTOR] }
```

Si el token es válido, el gateway agrega los headers `X-User-Id` y `X-User-Name`, así los servicios internos no tienen que parsear el JWT. Los **access tokens** duran 15 minutos y los **refresh tokens** 24 horas. Las claves RSA se pueden proveer por variables de entorno (`JWT_PRIVATE_KEY`, `JWT_PUBLIC_KEY`).

### Frontend
Proyecto independiente y desacoplado de la lógica de negocio:
- **Spring Boot MVC + Thymeleaf** con layouts (`layout/base.html`, `grid3.html`) y *fragments* reutilizables (tarjetas, modales, navbar).
- **Bootstrap 5**, CSS modular (`tokens`, `base`, `components` y un archivo por página), **Leaflet** para el mapa y **htmx** para interacciones parciales.
- Consume el gateway con `WebClient` desde una capa de servicios (`CollectionService`, `EventService`, `StatisticsService`, etc.) y maneja errores del backend con un `GlobalExceptionHandler`.
- Sesión y roles con **Spring Security** y un proveedor de autenticación propio que delega el login en `user-service`.

---

## Tecnologías

| Capa | Tecnologías |
|---|---|
| Lenguaje y build | Java 17, Maven (multi-módulo con wrapper) |
| Framework | Spring Boot 3.4.5, Spring Web, Spring Data JPA, Spring WebFlux (`WebClient`), Spring `@Scheduled` |
| Microservicios | Spring Cloud Gateway, Netflix Eureka (server y client) |
| Seguridad | Spring Security, JWT (jjwt, RS256) |
| Persistencia | MySQL 8, Hibernate / JPA |
| Archivos | Almacenamiento compatible con S3 (AWS SDK, MinIO en desarrollo), OpenCSV |
| Integraciones | API Georef (datos.gob.ar), API de desastres naturales de la cátedra |
| Frontend | Thymeleaf + Layout Dialect, Bootstrap 5.3, Leaflet 1.9, htmx, HTML5 / CSS3 / JS |
| Otros | Lombok, Apache Commons Text |

### Patrones y decisiones de diseño
- **Strategy**: algoritmos de consenso, generación de URLs, importadores de datasets.
- **Observer**: fuentes que notifican hechos nuevos al agregador.
- **Factory**: `SourceFactory`, `UserFactory`.
- **Adapter**: integración con APIs externas detrás de `IAPI` y con la API de ubicaciones.
- **Repositorio / Servicio / Controlador** en cada microservicio, con DTOs de entrada y salida separados de las entidades.
- Tareas **calendarizadas** para refresco de colecciones, recálculo de consenso, importación de datasets y estadísticas.

Los diagramas del proyecto están en [Miro](https://miro.com/app/board/uXjVIIto0Nc=/) y el documento de decisiones de diseño [acá](https://docs.google.com/document/d/1wWerL9a8_lSPYtvtudA35wlBbKm6wkR2Knrf753ctXE/edit?tab=t.0#heading=h.aczyuw2yex2w).

---

## Cómo ejecutarlo

### Requisitos
- JDK 17
- MySQL 8 en `localhost:3306`
- MinIO (u otro almacenamiento compatible con S3) en `localhost:9000` con un bucket `metamapa-media`, solo para `dynamic-source`

### Configuración
Cada módulo trae un `application.properties.example`. Copialo como `application.properties` en la misma carpeta y completá usuario y contraseña de MySQL y, si hace falta, URLs y puertos. En `statistics-service` y `aggregation-service` revisá `aggregation-service.url` y `api.gateway.url`.

### Orden de arranque
Desde `backend/`:

```bash
./mvnw -pl server-registry spring-boot:run     # 1. Eureka
./mvnw -pl user-service spring-boot:run        # 2. Usuarios
./mvnw -pl aggregation-service spring-boot:run # 3. Agregador
./mvnw -pl static-source spring-boot:run       # 4. Fuentes
./mvnw -pl dynamic-source spring-boot:run
./mvnw -pl proxy-source spring-boot:run
./mvnw -pl statistics-service spring-boot:run  # 5. Estadísticas
./mvnw -pl api-gateway spring-boot:run         # 6. Gateway
```

Y el cliente web, desde `frontend/`:

```bash
./mvnw spring-boot:run
```

Con todo arriba, la aplicación queda en **http://localhost:8085**.

---

## Estructura del repositorio

```
.
├── backend/                  # Maven multi-módulo
│   ├── api-gateway/
│   ├── server-registry/
│   ├── user-service/
│   ├── aggregation-service/
│   ├── static-source/
│   ├── dynamic-source/
│   ├── proxy-source/
│   └── statistics-service/
└── frontend/                 # Cliente web (Thymeleaf)
```

## Equipo

Grupo 18 · UTN FRBA · 2025
- Facundo Diez Forradellas
- Valentin Osa
- Mateo Hernandez
- Lautaro Dario Cicala Fontao
