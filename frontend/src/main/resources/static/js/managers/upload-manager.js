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
            errorMsg.textContent = `⚠️ Solo puedes subir un máximo de ${MAX_FILES} evidencias.`; //! esto deberia manejarse mejor, ya sea bloqueando el boton de media o con un popup
            errorMsg.style.display = 'block';

            return;
        }

        // 2. Procesar archivos
        const validNewFiles = []; // Lista temporal para los nuevos
        for (let i = 0; i < newFiles.length; i++) {
            const file = newFiles[i];

            if (!file.type.startsWith('image/') && !file.type.startsWith('video/')) {
                continue;
            }

            dt.items.add(file);       // 1. Guardamos en el general
            validNewFiles.push(file); // 2. Guardamos en la lista temporal para animar
        }

        // 3. La actualización del input y la llamada a la nueva función de cascada
        document.getElementById('mediaInput').files = dt.files;

        // Calculamos dónde empiezan los nuevos índices (si ya tenía 2 fotos, los nuevos son 2, 3...)
        const startIndex = dt.items.length - validNewFiles.length;

        // Llamamos a la nueva función que anima
        appendNewFiles(validNewFiles, startIndex);
    }

    function appendNewFiles(files, startIndex) {
        const container = document.getElementById('media-preview-container');

        files.forEach((file, i) => {
            // setTimeout multiplicado por el índice (i)
            // Foto 0: 0ms (inmediato)
            // Foto 1: 100ms
            // Foto 2: 200ms...
            setTimeout(() => {
                // Calculamos el índice global para que el botón de borrar sepa cuál es
                const globalIndex = startIndex + i;

                // Creamos el HTML
                const itemElement = createPreviewElement(file, globalIndex);

                // Lo agregamos al final SIN borrar lo anterior
                container.appendChild(itemElement);
            }, i * 100);
        });
    }

    function createPreviewElement(file, index) {
        const url = URL.createObjectURL(file);

        const item = document.createElement('div');
        item.className = 'preview-item';

        // Forzamos la animación 'popIn' definida en el CSS
        item.style.animation = 'popIn 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards';

        let media;

        const type = file.type.startsWith('video/') ? 'video' : 'image';

        if (type === 'video') {
            media = document.createElement('video');
            media.src = url;
            media.muted = true;
            media.onmouseover = () => media.play();
            media.onmouseout = () => { media.pause(); media.currentTime = 0; };
        } else {
            media = document.createElement('img');
            media.src = url;
        }

        // Le damos estilo pointer para que sepa que es clickeable
        media.style.cursor = 'pointer';

        media.onclick = (e) => {
            // Evitamos que el click se propague (buena práctica)
            e.stopPropagation();
            openGalleryWithPreview(index);
        };

        const btn = document.createElement('button');
        btn.className = 'preview-remove-btn';
        btn.innerHTML = '×';
        btn.type = "button";
        btn.onclick = () => removeFile(index); // Le asignamos su índice correcto

        item.appendChild(media);
        item.appendChild(btn);

        return item;
    }

    function openGalleryWithPreview(clickedIndex) {
        // 1. Convertimos 'dt.files' a un Array de objetos simple
        const playlist = [];
        const files = dt.files;

        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            playlist.push({
                url: URL.createObjectURL(file), // Usamos la URL temporal del navegador
                type: file.type.startsWith('video/') ? 'video' : 'image'
            });
        }

        // 2. Llamamos a la Galería Global pasándole la lista directa
        if (typeof MediaGallery !== 'undefined') {
            MediaGallery.open(playlist, clickedIndex);
        } else {
            console.error("MediaGallery no está cargada.");
        }
    }

    function removeFile(indexToRemove) {
        const container = document.getElementById('media-preview-container');
        const items = container.children; // Obtenemos los elementos HTML actuales

        // Seguridad: si el elemento no existe, salimos
        if (!items[indexToRemove]) return;

        const itemToRemove = items[indexToRemove];

        // 1. ANIMACIÓN: Le ponemos la clase CSS que lo encoge
        itemToRemove.classList.add('removing');

        // 2. LÓGICA: Esperamos 300ms (lo que dura la transición CSS)
        setTimeout(() => {
            // A. Actualizamos la memoria interna (DataTransfer)
            const newDt = new DataTransfer();
            const files = dt.files;
            for (let i = 0; i < files.length; i++) {
                if (i !== indexToRemove) {
                    newDt.items.add(files[i]);
                }
            }
            dt = newDt;

            // B. Actualizamos el input oculto
            document.getElementById('mediaInput').files = dt.files;

            // C. Borramos solo este nodo del HTML
            itemToRemove.remove();

            // D. Re-calculamos los índices de los botones restantes
            reindexButtons();

            // Ocultar mensaje de error si ya no superamos el límite
            document.getElementById('media-error-msg').style.display = 'none';

        }, 300); // Tiene que coincidir con los 0.3s del CSS
    }

    // Función para corregir los botones después de borrar uno
    function reindexButtons() {
        const container = document.getElementById('media-preview-container');
        const items = container.children;

        for (let i = 0; i < items.length; i++) {
            const btn = items[i].querySelector('.preview-remove-btn');
            // Actualizamos el índice para que apunte a la nueva posición 'i'
            btn.onclick = () => removeFile(i);
        }
    }

    // Exponemos solo lo necesario
    return {
        handleFiles
    };

})();