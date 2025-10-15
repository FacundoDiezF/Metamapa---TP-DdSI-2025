/**
 * INFINITE SCROLL - Sistema de paginación automática para el feed de eventos
 * Usa Intersection Observer API para detectar cuando el usuario llega al final
 */

(function() {
    'use strict';

    // Estado de la paginación
    let currentPage = 0;
    let isLoading = false;
    let hasMorePages = true;

    // Referencias a elementos del DOM
    const eventsContainer = document.getElementById('events-container');
    const loadingIndicator = document.getElementById('loading-indicator');
    const endMessage = document.getElementById('end-message');
    const sentinel = document.getElementById('scroll-sentinel');

    // Verificar que todos los elementos existan
    if (!eventsContainer || !loadingIndicator || !endMessage || !sentinel) {
        console.warn('Infinite scroll: Elementos DOM no encontrados');
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

            // Extraer solo las cards de eventos nuevas
            const newEvents = doc.querySelectorAll('#events-container > *');

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
     * Configurar Intersection Observer
     * Detecta cuando el "sentinel" entra en el viewport
     */
    const observerOptions = {
        root: null, // Usar el viewport como root
        rootMargin: '200px', // Cargar cuando esté a 200px de ser visible
        threshold: 0.01 // Activar cuando el 1% sea visible
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting && !isLoading && hasMorePages) {
                console.log('📍 Sentinel visible, cargando más eventos...');
                loadNextPage();
            }
        });
    }, observerOptions);

    // Comenzar a observar el sentinel
    observer.observe(sentinel);

    // Cleanup cuando la página se descarga
    window.addEventListener('beforeunload', () => {
        observer.disconnect();
    });

    console.log('✓ Infinite scroll inicializado');

})();

