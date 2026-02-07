(function() {
    // Variable para guardar el observador y no crearlo mil veces
    let videoObserver;

    function initLazyVideos() {
        // Seleccionamos videos que tengan la clase .lazy-video Y que no hayan sido procesados aún
        // (Podemos marcar los procesados quitándoles la clase o añadiendo un atributo data-loaded)
        const lazyVideos = document.querySelectorAll("video.lazy-video:not([data-observed='true'])");

        if (lazyVideos.length === 0) return;

        if ("IntersectionObserver" in window) {
            // Si no existe el observador, lo creamos
            if (!videoObserver) {
                videoObserver = new IntersectionObserver((entries, observer) => {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            const video = entry.target;

                            // Solo cargamos si no se ha cargado ya
                            if (video.getAttribute('preload') === 'none') {
                                video.setAttribute('preload', 'metadata'); // Carga solo el header del video
                                video.load(); // Fuerza al navegador a buscar la portada
                            }

                            // Dejamos de observar este video específico
                            observer.unobserve(video);
                        }
                    });
                }, {
                    rootMargin: "0px 0px 200px 0px" // Pre-carga cuando el video está a 200px de entrar en pantalla
                });
            }

            lazyVideos.forEach(video => {
                videoObserver.observe(video);
                video.setAttribute('data-observed', 'true'); // Marcamos para no volver a agregarlo
            });
        } else {
            // Fallback para navegadores antiguos
            lazyVideos.forEach(video => {
                video.setAttribute('preload', 'metadata');
            });
        }
    }

    // Ejecutar al inicio
    document.addEventListener("DOMContentLoaded", initLazyVideos);

    // Ejecutar cada vez que HTMX inyecta contenido nuevo (Infinite Scroll)
    document.body.addEventListener("htmx:afterOnLoad", initLazyVideos);

})();