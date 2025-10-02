# 🌍 MetaMapa Frontend

Frontend de **MetaMapa**, basado en **Spring Boot MVC + Thymeleaf + Bootstrap**.  
Toda la interfaz se construye con **layouts Thymeleaf**, **fragments reutilizables** y **CSS modularizado**.


---

## 🎨 Guía de estilos

### 1. `tokens.css`
📍 **Colores y fuentes globales**  
Contiene:
- Paleta de colores (`--ui-blue`, `--ui-accent`, etc.).
- Tipografías (familia `--font-sans` y pesos `--font-weight-*`).

### 2. `base.css`
📍 **Reglas globales y semánticas**  
Incluye:
- Reset (`box-sizing`, márgenes base).
- Escala tipográfica (`h1`, `h2`, `h3`, body, small).
- Layout (`.container`, `.section`).
- Formularios base (`input`, `textarea`, `button`).
- Helpers de accesibilidad (`.visually-hidden`).
- Focus ring unificado.

👉 Usar para cambios de **estructura tipográfica, layout global o formularios**.

### 3. `components.css`
📍 **Componentes de UI reutilizables**  
Incluye:
- **Botones** (`.btn`, `.btn--primary`, `.btn--ghost`).
- **Cards** (`.card`, `.card.sm`, `.card.lg`).
- **Badges/Pills** (`.badge`, `.badge.warn`, `.badge.error`).

👉 Usar para **botones, tarjetas o etiquetas**.

### 4. `[page].css` (ej: `landing.css`)
📍 **Estilos específicos de cada vista**  
Cada página puede tener su propio CSS.  
👉 Usar sólo para lo que no sea global/reutilizable.

---

## 🛠️ Convenciones

- ❌ No usar `utilities.css`: Bootstrap ya provee utilidades (`d-flex`, `gap-2`, `w-100`, etc.).
- ✅ Variantes con **BEM**: `btn--primary`, `btn--ghost` (no sobrescribir `.btn-primary` de Bootstrap).
- ✅ Orden de carga de estilos en `base.html`:
  ```html
  <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}" />
  <link rel="stylesheet" th:href="@{/css/tokens.css}" />
  <link rel="stylesheet" th:href="@{/css/base.css}" />
  <link rel="stylesheet" th:href="@{/css/components.css}" />
  <section layout:fragment="pageStyles"></section>

---

## 🚀 Cómo correr el front
- En la raíz del proyecto (donde está pom.xml):
````
./mvnw spring-boot:run
````
Abrir en navegador:

Landing: http://localhost:8080/

## ✅ Dónde editar estilos

- Colores globales / fuentes → tokens.css 
- Títulos, párrafos, layout, forms → base.css 
- Botones, cards, badges → components.css 
- Estilos específicos de una vista → [page].css (ej: landing.css)