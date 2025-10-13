document.addEventListener('DOMContentLoaded', applyExploreLayoutFix);
window.addEventListener('load', applyExploreLayoutFix);
window.addEventListener('resize', function () { setTimeout(applyExploreLayoutFix, 100); });

0function pxToNumber(px) {
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

        // obtener altura del post-box y su top offset (si es sticky)
        const postBox = share.querySelector('.post-box') || share;
        const postBoxH = (postBox && postBox.offsetHeight) ? postBox.offsetHeight : 72;
        const computedTop = postBox ? getComputedStyle(postBox).top : '0px';
        const topOffset = pxToNumber(computedTop);
        const totalOffset = postBoxH + topOffset + 12; // padding extra

        if (firstHecho) {
          // aplicar con prioridad para sobreescribir otras reglas
          firstHecho.style.setProperty('margin-top', totalOffset + 'px', 'important');
        } else {
          // si no encontramos .hecho directo, intentar ajustar la primera .hecho dentro del feed
          const anyHecho = feed.querySelector('.hecho');
          if (anyHecho) {
            anyHecho.style.setProperty('margin-top', totalOffset + 'px', 'important');
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
