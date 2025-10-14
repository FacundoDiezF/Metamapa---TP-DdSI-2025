document.addEventListener('DOMContentLoaded', applyExploreLayoutFix);
window.addEventListener('load', applyExploreLayoutFix);
window.addEventListener('resize', function () { setTimeout(applyExploreLayoutFix, 100); });

// Observar cambios en el estado del share (abierto/cerrado)
document.addEventListener('DOMContentLoaded', function() {
  const share = document.querySelector('.share');
  if (share) {
    // Listener para cuando se abre/cierra el details
    share.addEventListener('toggle', function() {
      setTimeout(applyExploreLayoutFix, 50);
    });

    // Observar cambios en el DOM del share-panel por si se agrega contenido dinámicamente
    const observer = new MutationObserver(function() {
      applyExploreLayoutFix();
    });

    observer.observe(share, {
      childList: true,
      subtree: true,
      attributes: true,
      attributeFilter: ['open', 'style']
    });
  }
});

function pxToNumber(px) {
  if (!px) return 0;
  const n = parseFloat(px.replace('px',''));
  return isNaN(n) ? 0 : n;
}

function applyExploreLayoutFix() {
  try {
    // Ajustar margen de la primera tarjeta .hecho que siga al .share para evitar solapamiento
    const feed = document.querySelector('.feed');
    if (feed) {
      const share = feed.querySelector('.share');
      if (share) {
        // buscar el siguiente elemento .hecho (puede estar envuelto en contenedor)
        let next = share.nextElementSibling;
        let firstHecho = null;
        while (next) {
          if (next.classList && next.classList.contains('hecho')) { firstHecho = next; break; }
          const inside = next.querySelector && next.querySelector('.hecho');
          if (inside) { firstHecho = inside; break; }
          next = next.nextElementSibling;
        }

        // Si el share está cerrado, no necesitamos offset extra
        const isOpen = share.hasAttribute('open');

        if (firstHecho) {
          if (isOpen) {
            // Cuando está abierto, calcular el espacio necesario para el panel
            const sharePanel = share.querySelector('.share-panel');
            if (sharePanel) {
              // Esperar un momento para que el panel se renderice completamente
              setTimeout(() => {
                const panelHeight = sharePanel.offsetHeight;
                const postBoxHeight = share.querySelector('.post-box')?.offsetHeight || 72;
                // Agregar margen suficiente para que el primer evento esté debajo del panel
                // Aumentamos el margen: ahora sumamos las alturas completas más un espacio adicional
                const totalMargin = panelHeight + postBoxHeight + 20;
                firstHecho.style.setProperty('margin-top', `${totalMargin}px`, 'important');
              }, 10);
            } else {
              firstHecho.style.setProperty('margin-top', '8px', 'important');
            }
          } else {
            // Cuando está cerrado, usar el margen por defecto del CSS
            firstHecho.style.removeProperty('margin-top');
          }
        }
      }
    }

    // Forzar altura razonable del mapa y limitar la sidebar para evitar que el mapa cause scroll de página
    const map = document.getElementById('map');
    if (map) {
      // aplicar con prioridad (mantener valor si ya existe)
      map.style.setProperty('height', map.style.height || '180px', 'important');
      map.style.setProperty('max-height', '260px', 'important');
      map.style.setProperty('min-height', '110px', 'important');
    }

    const sidebar = document.querySelector('.sidebar-right');
    if (sidebar) {
      const header = document.querySelector('header') || document.querySelector('.site-header');
      const headerH = header ? header.offsetHeight : 80;
      const topOffset = headerH + 24; // margen extra
      sidebar.style.setProperty('max-height', `calc(100vh - ${topOffset}px)` , 'important');
      sidebar.style.setProperty('overflow-y', 'auto', 'important');
    }
  } catch (e) {
    console.warn('explore-layout-fix error', e);
  }
}
