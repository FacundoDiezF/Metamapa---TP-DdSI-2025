# Estilos compartidos

## Capas
1. tokens.css → variables globales (colores, tipografías, espaciados, sombras).
2. base.css → reset mínimo, tipografía, contenedor, formularios.
3. components.css → UI reusables (.card, .btn, .badge, .grid-2/3, focus visible).
4. utilities.css → clases pequeñas (espaciado, display, borde, sombra, texto).

## Orden de importación
tokens → base → components → utilities → nav/icons/map → css del módulo.

## Reglas
- Prohibido redefinir `:root`, resets, `.card`, `.btn` en módulos.
- Si un módulo necesita variantes, usar clases extra (ej. `.card.landing`).
- Accesibilidad: `:focus-visible` viene activado globalmente.
