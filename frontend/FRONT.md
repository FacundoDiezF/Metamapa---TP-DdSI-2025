# 🌍 MetaMapa Frontend

Frontend de **MetaMapa**, basado en **Spring Boot MVC + Thymeleaf + Bootstrap**.  
Toda la interfaz se construye con **layouts Thymeleaf**, **fragments reutilizables** y **CSS modularizado**.


---

## 🎨 Guía de estilos

Perfecto 🚀
Ahora que ya tenemos el desglose detallado de roles de cada `.css`, te armo una versión breve para un **README.md**, pensada para el repo de tu proyecto.

---

# 🎨 Guía CSS · MetaMapa

Este proyecto usa **Spring Boot MVC + Thymeleaf + Bootstrap**.
Los estilos se dividen en **archivos compartidos** y **archivos específicos por página**.

## 📂 Archivos compartidos

### **`tokens.css`**
  Variables globales: colores, tipografías, radios, sombras, layout tokens (`--Accent`, `--font-sans`, `--header-h`).
  👉 Fuente de verdad del diseño. No incluye resets ni componentes.

### **`base.css`**
  Reset y fundamentos: normalización (`*`, `html`, `body`), escala tipográfica, accesibilidad (`:focus-visible`), formularios básicos.
  👉 No contiene colores ni componentes.

### **`components.css`**
  UI reutilizable: `.btn`, `.btn--primary`, `.btn--ghost`, `.card`, `.badge`, `.pill`.
  👉 Componentes atómicos, sin estilos de página.

### **`icons.css`**
  Estilos de íconos: tamaños (`.icon-sm`, `.icon-lg`), colores (`.icon--accent`, `.icon--muted`).
  👉 No define paths ni assets.

### **`nav.css`**
  Barra de navegación: `.mm-nav`, `.mm-brand`, `.mm-btn`, variantes (`.mm-nav--with-center`).
  👉 Scoped con prefijo `mm-`.

### **`footer.css`** *(futuro)*
  Grid y estructura del footer: `.footer__grid`, `.footer__brand`, `.footer__col`.
  👉 Solo aplica al footer.

## 🗂 Archivos por página

Cada página (Landing, Mapa, Colecciones, etc.) tiene su propio `.css` en `static/css/`.
Ejemplo: `landing.css` → estilos específicos de la landing.

## 🔑 Regla de oro

* `tokens.css` → variables globales.
* `base.css` → resets y fundamentos.
* `components.css` → piezas reusables.
* `icons.css`, `nav.css`, `footer.css` → secciones globales.
* `.css` por página → estilos locales.

❌ Nunca sobrescribir `.container`, `.row`, `.card` de Bootstrap.
✔️ Si hace falta, scopear (ej: `.featured .card { ... }`).

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