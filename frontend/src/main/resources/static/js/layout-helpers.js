// layout-helpers.js
// Calcula la altura real del elemento .post-box (summary sticky) y la exporta
// como variable CSS --post-box-h en :root para que el feed pueda usarla
(function () {
  'use strict';

  function setPostBoxHeight() {
    try {
      var post = document.querySelector('.post-box');
      if (!post) return;
      var rect = post.getBoundingClientRect();
      // añadir un pequeño margen de seguridad (6px)
      var h = Math.ceil(rect.height) + 6;
      document.documentElement.style.setProperty('--post-box-h', h + 'px');
    } catch (e) {
      // silencioso
      console.warn('layout-helpers: error computing post-box height', e);
    }
  }

  // Observador para cambios de tamaño del post-box (cuando se abre el detalle o cambia contenido)
  function observePostBox() {
    var post = document.querySelector('.post-box');
    if (!post) return;
    try {
      var ro = new ResizeObserver(setPostBoxHeight);
      ro.observe(post);
    } catch (e) {
      // ResizeObserver no soportado -> fallback a resize y mutation
      window.addEventListener('resize', setPostBoxHeight);
      var details = post.closest('details');
      if (details) {
        details.addEventListener('toggle', setPostBoxHeight);
      }
    }
  }

  // Ejecutar en DOMContentLoaded y con un pequeño timeout para casos reflow
  document.addEventListener('DOMContentLoaded', function () {
    setPostBoxHeight();
    observePostBox();
    // fallback adicional por si el layout tarda en renderizar imágenes o fonts
    setTimeout(setPostBoxHeight, 300);
    setTimeout(setPostBoxHeight, 1200);
  });

})();

