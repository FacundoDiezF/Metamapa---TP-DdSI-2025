const UploadManager = (function() {

    // Configuración
    const MAX_FILES = 5;

    // Almacén de archivos (DataTransfer nos permite manipular el input file)
    let dt = new DataTransfer();

    function handleFiles(newFiles) {
        const errorMsg = document.getElementById('media-error-msg');
        errorMsg.style.display = 'none'; // Reseteamos errores previos

        // 1. VALIDACIÓN: Cantidad Máxima
        if (dt.items.length + newFiles.length > MAX_FILES) {
            errorMsg.textContent = `⚠️ Solo puedes subir un máximo de ${MAX_FILES} evidencias.`;
            errorMsg.style.display = 'block';

            // Opcional: Vibración en celular para feedback táctil
            if (navigator.vibrate) navigator.vibrate(200);
            return; // Detenemos la función, no agregamos nada
        }

        // 2. Procesar archivos
        for (let i = 0; i < newFiles.length; i++) {
            const file = newFiles[i];

            // VALIDACIÓN: Tipo de archivo (Doble chequeo por seguridad)
            if (!file.type.startsWith('image/') && !file.type.startsWith('video/')) {
                continue; // Saltamos archivos que no sean imagen/video
            }

            // Si pasa, lo agregamos al DataTransfer
            dt.items.add(file);
        }

        // 3. Sincronizar input real y renderizar
        updateInputAndRender();
    }

    function removeFile(index) {
        // Truco: Para borrar, creamos un nuevo DataTransfer y copiamos todos MENOS el borrado
        const newDt = new DataTransfer();
        const files = dt.files;

        for (let i = 0; i < files.length; i++) {
            if (i !== index) { // Copiamos todos menos el que coincide con el índice
                newDt.items.add(files[i]);
            }
        }

        dt = newDt; // Actualizamos el almacén principal
        updateInputAndRender();

        // Ocultar error si bajamos del límite (por si había quedado alguno pegado)
        document.getElementById('media-error-msg').style.display = 'none';
    }

    function updateInputAndRender() {
        // A. Actualizamos el input HTML invisible para que se envíe al backend
        document.getElementById('mediaInput').files = dt.files;

        // B. Renderizamos las miniaturas visuales
        const container = document.getElementById('media-preview-container');
        container.innerHTML = ''; // Limpiar

        for (let i = 0; i < dt.files.length; i++) {
            const file = dt.files[i];
            const url = URL.createObjectURL(file);

            const item = document.createElement('div');
            item.className = 'preview-item';

            // Determinar si es video o imagen
            let media;
            if (file.type.startsWith('video/')) {
                media = document.createElement('video');
                media.src = url;
                media.muted = true; // Video mudo en la preview
                media.onmouseover = () => media.play(); // Play al pasar mouse (efecto cool)
                media.onmouseout = () => { media.pause(); media.currentTime = 0; };
            } else {
                media = document.createElement('img');
                media.src = url;
            }

            // Botón Borrar
            const btn = document.createElement('button');
            btn.className = 'preview-remove-btn';
            btn.innerHTML = '×';
            // Importante: type="button" para que no haga submit del form
            btn.type = "button";
            btn.onclick = () => removeFile(i);

            item.appendChild(media);
            item.appendChild(btn);
            container.appendChild(item);
        }
    }

    // Exponemos solo lo necesario
    return {
        handleFiles
    };

})();