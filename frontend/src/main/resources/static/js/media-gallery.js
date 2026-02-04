const MediaGallery = (function() {

    let currentPlaylist = [];
    let currentIndex = 0;
    let isZoomed = false;

    let modal;
    let contentContainer;
    let counter;
    let currentImageElement = null; // Guardamos referencia a la imagen actual

    // === FUNCIONES PÚBLICAS ===

    function open(playlistElement, startIndex = 0) {
        if (!contentContainer) initElements();

        currentPlaylist = [];
        const items = playlistElement.querySelectorAll('span');
        items.forEach(item => {
            currentPlaylist.push({
                url: item.getAttribute('data-url'),
                type: item.getAttribute('data-type')
            });
        });

        currentIndex = parseInt(startIndex, 10);

        if (isNaN(currentIndex) || currentIndex < 0 || currentIndex >= currentPlaylist.length) {
            currentIndex = 0;
        }

        renderCurrentItem();

        if (modal) {
            modal.classList.add('active');
            document.body.style.overflow = 'hidden';
            document.addEventListener('keydown', handleKeydown);
        }
    }

    function close() {
        if (modal) modal.classList.remove('active');
        resetZoom();
        if (contentContainer) contentContainer.innerHTML = '';
        document.body.style.overflow = '';
        document.removeEventListener('keydown', handleKeydown);
    }

    function next() {
        currentIndex = (currentIndex + 1) % currentPlaylist.length;
        renderCurrentItem();
    }

    function prev() {
        currentIndex = (currentIndex - 1 + currentPlaylist.length) % currentPlaylist.length;
        renderCurrentItem();
    }

    // === ZOOM & PAN LOGIC ===

    function toggleZoom(e) {
        // Solo aplica para imágenes
        if (!currentImageElement) return;

        isZoomed = !isZoomed;

        if (isZoomed) {
            contentContainer.classList.add('is-zoomed');
            // Al activar, hacemos un primer ajuste inmediato a donde estaba el mouse
            updatePanPosition(e);
        } else {
            contentContainer.classList.remove('is-zoomed');
            // Reseteamos posición al centro
            setTimeout(() => {
                if (currentImageElement) currentImageElement.style.transformOrigin = 'center center';
            }, 300); // Esperamos que termine la transición de salida
        }
    }

    function resetZoom() {
        isZoomed = false;
        if (contentContainer) contentContainer.classList.remove('is-zoomed');
        if (currentImageElement) currentImageElement.style.transformOrigin = 'center center';
    }

    // Esta es la magia: Mueve la imagen según el mouse
    function handleMouseMove(e) {
        if (!isZoomed || !currentImageElement) return;
        updatePanPosition(e);
    }

    function updatePanPosition(e) {
        // Obtenemos las dimensiones del contenedor (la pantalla visible)
        const rect = contentContainer.getBoundingClientRect();

        // Calculamos dónde está el mouse relativo al contenedor (0 a 100%)
        // e.clientX es la posición global X del mouse
        // rect.left es dónde empieza el contenedor
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;

        const xPercent = (x / rect.width) * 100;
        const yPercent = (y / rect.height) * 100;

        // Movemos el "punto de origen" de la transformación (el zoom) hacia el mouse
        currentImageElement.style.transformOrigin = `${xPercent}% ${yPercent}%`;
    }

    // === FUNCIONES PRIVADAS ===

    function initElements() {
        modal = document.getElementById('mediaGalleryModal');
        contentContainer = document.getElementById('mediaModalContent');
        counter = document.getElementById('mediaModalCounter');

        // Listener para el movimiento del mouse (PAN)
        if (contentContainer) {
            contentContainer.addEventListener('mousemove', handleMouseMove);
        }
    }

    function renderCurrentItem() {
        if (!contentContainer) return;

        resetZoom();
        currentImageElement = null; // Limpiamos referencia

        const item = currentPlaylist[currentIndex];
        contentContainer.innerHTML = '';

        let mediaElement;

        if (item.type === 'video') {
            mediaElement = document.createElement('video');
            mediaElement.src = item.url;
            mediaElement.controls = true;
            mediaElement.autoplay = true;
            mediaElement.style.maxWidth = '100%';
            mediaElement.style.maxHeight = '90vh';
        } else {
            // IMAGEN
            mediaElement = document.createElement('img');
            mediaElement.src = item.url;
            mediaElement.style.objectFit = 'contain';

            // Animación entrada
            mediaElement.style.opacity = '0';
            mediaElement.style.transition = 'opacity 0.2s ease, transform 0.3s ease'; // Añadimos transform a la transición

            mediaElement.onload = () => mediaElement.style.opacity = '1';
            setTimeout(() => mediaElement.style.opacity = '1', 50);

            // === HABILITAMOS ZOOM PARA TODAS ===
            // Ya no verificamos tamaño. Si es imagen, tiene zoom.
            mediaElement.onclick = toggleZoom;

            // Guardamos la referencia global para usarla en el mousemove
            currentImageElement = mediaElement;
        }

        contentContainer.appendChild(mediaElement);

        if (counter) {
            counter.textContent = `${currentIndex + 1} / ${currentPlaylist.length}`;
        }
    }

    function handleKeydown(e) {
        if (e.key === 'Escape') close();
        // Bloqueamos flechas si está con zoom para evitar mareos
        if (!isZoomed) {
            if (e.key === 'ArrowRight') next();
            if (e.key === 'ArrowLeft') prev();
        } else {
            // Si está con zoom, cualquier tecla de navegación lo cierra
            if (e.key === 'ArrowRight' || e.key === 'ArrowLeft') resetZoom();
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        initElements();

        document.body.addEventListener('click', (e) => {
            const gridItem = e.target.closest('.media-grid-item');
            if (gridItem) {
                const grid = gridItem.closest('.media-grid');
                if (grid) {
                    const playlistContainer = grid.querySelector('.gallery-playlist');
                    const indexClicked = gridItem.dataset.index;

                    if (playlistContainer && indexClicked !== undefined) {
                        open(playlistContainer, indexClicked);
                    }
                }
            }
        });
    });

    return { close, next, prev };

})();