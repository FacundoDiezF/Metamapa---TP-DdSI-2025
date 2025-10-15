/**
 * INFINITE SCROLL - Sistema de paginación automática para el feed de eventos
 * Usa scroll event para detectar cuando el usuario llega al final del feed
 */

(function() {
    'use strict';

    // Estado de la paginación
    let currentPage = 0;
    let isLoading = false;
    let hasMorePages = true;

    // Referencias a elementos del DOM
    const feedContainer = document.querySelector('.feed');
    const eventsContainer = document.getElementById('events-container');
    const loadingIndicator = document.getElementById('loading-indicator');
    const endMessage = document.getElementById('end-message');
    const sentinel = document.getElementById('scroll-sentinel');

    // Verificar que todos los elementos existan
    if (!feedContainer || !eventsContainer || !loadingIndicator || !endMessage || !sentinel) {
        console.warn('Infinite scroll: Elementos DOM no encontrados');
        console.log('feedContainer:', feedContainer);
        console.log('eventsContainer:', eventsContainer);
        console.log('loadingIndicator:', loadingIndicator);
        console.log('endMessage:', endMessage);
        console.log('sentinel:', sentinel);
        return;
    }

    /**
     * Carga la siguiente página de eventos
     */
    async function loadNextPage() {
        if (isLoading || !hasMorePages) return;

        isLoading = true;
        currentPage++;

        // Mostrar indicador de carga
        loadingIndicator.style.display = 'flex';

        try {
            // Obtener los parámetros actuales de la URL (filtros, etc.)
            const urlParams = new URLSearchParams(window.location.search);
            urlParams.set('page', currentPage);

            // Hacer petición al servidor
            const response = await fetch(`${window.location.pathname}?${urlParams.toString()}`, {
                headers: {
                    'X-Requested-With': 'XMLHttpRequest' // Para indicar que es una petición AJAX
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const html = await response.text();

            // Parsear el HTML recibido
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');

            // Extraer solo las cards de eventos nuevas (buscar directamente las cards .hecho)
            const newEvents = doc.querySelectorAll('.hecho.card');

            if (newEvents.length === 0) {
                // No hay más eventos, mostrar mensaje final
                hasMorePages = false;
                endMessage.style.display = 'flex';
                sentinel.style.display = 'none';
            } else {
                // Agregar los nuevos eventos al contenedor
                newEvents.forEach(event => {
                    eventsContainer.appendChild(event.cloneNode(true));
                });

                console.log(`✓ Cargados ${newEvents.length} eventos (página ${currentPage})`);
            }

        } catch (error) {
            console.error('Error al cargar más eventos:', error);
            currentPage--; // Revertir el incremento de página

            // Mostrar mensaje de error al usuario (opcional)
            const errorMsg = document.createElement('div');
            errorMsg.className = 'loading-indicator';
            errorMsg.innerHTML = '<p style="color: var(--Danger);">⚠️ Error al cargar más eventos. Intenta de nuevo.</p>';
            eventsContainer.appendChild(errorMsg);

            setTimeout(() => errorMsg.remove(), 3000);
        } finally {
            isLoading = false;
            loadingIndicator.style.display = 'none';
        }
    }

    /**
     * Verifica si el usuario ha llegado cerca del final del scroll
     */
    function checkScrollPosition() {
        if (!feedContainer || isLoading || !hasMorePages) return;

        const scrollTop = feedContainer.scrollTop;
        const scrollHeight = feedContainer.scrollHeight;
        const clientHeight = feedContainer.clientHeight;

        // Calcular cuánto falta para llegar al final (en píxeles)
        const distanceFromBottom = scrollHeight - (scrollTop + clientHeight);

        // Si está a menos de 300px del final, cargar más eventos
        if (distanceFromBottom < 300) {
            console.log('📍 Cerca del final del feed, cargando más eventos...');
            console.log(`   scrollTop: ${scrollTop}, scrollHeight: ${scrollHeight}, clientHeight: ${clientHeight}`);
            console.log(`   distancia del final: ${distanceFromBottom}px`);
            loadNextPage();
        }
    }

    // Usar debounce para no ejecutar la función en cada evento de scroll
    let scrollTimeout;
    function handleScroll() {
        if (scrollTimeout) {
            clearTimeout(scrollTimeout);
        }
        scrollTimeout = setTimeout(checkScrollPosition, 100);
    }

    // Agregar event listener al contenedor del feed
    feedContainer.addEventListener('scroll', handleScroll);

    // Cleanup cuando la página se descarga
    window.addEventListener('beforeunload', () => {
        feedContainer.removeEventListener('scroll', handleScroll);
    });

    console.log('✓ Infinite scroll inicializado con scroll event listener');
    console.log(`   Feed container:`, feedContainer);
    console.log(`   scrollHeight: ${feedContainer.scrollHeight}px, clientHeight: ${feedContainer.clientHeight}px`);

})();
