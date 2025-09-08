# Instrucciones para mensajes de commit en Git

Por favor, genera mensajes de commit siguiendo estas reglas:

## Formato del título
- Usa el estándar [Conventional Commits](https://www.conventionalcommits.org/):
   - `feat:` → nueva funcionalidad visible para el usuario.
   - `fix:` → corrección de errores visible para el usuario.
   - `docs:` → cambios en documentación.
   - `style:` → cambios de formato, sin modificar la lógica (espacios, punto y coma, etc.).
   - `refactor:` → refactorización de código, sin cambio en la funcionalidad.
   - `test:` → agregar o modificar pruebas, sin afectar el código productivo.
   - `chore:` → tareas de mantenimiento, configuración, dependencias, etc.

## Estilo de escritura
- Usa **modo imperativo** (ejemplo: "Agregar validación" en vez de "Agregado").
- El título debe ser **claro y breve**, máximo **50 caracteres**.
- Explica el propósito principal del cambio en el título.

## Cuerpo del commit
- Usa viñetas para listar los cambios relevantes.
- Sé conciso pero informativo, sin repetir el título.
- Separa el cuerpo del título con una línea en blanco.

## Co-autores
Siempre agrega esta sección al final del commit:  
Co-authored-by: Valentín Rodrigo Osa Pochelú <valenosa@users.noreply.github.com>
Co-authored-by: Mateo Nicolás Hernandez <mateonhernandez@frba.utn.edu.ar>
Co-authored-by: Lucas Nahuel Devecchi Di Bella <ldevecchi@frba.utn.edu.ar>
Co-authored-by: Facundo Diez Forradellas <FacundoDiezF@users.noreply.github.com>
Co-authored-by: Lautaro Dario Cicala Fontao <lcicalafontao@frba.utn.edu.ar>

## Idioma
- Todo el commit debe escribirse en **español**. 