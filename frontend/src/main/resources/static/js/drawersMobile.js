// =================================================================
// Cajon: mobile sidebar toggle y backdrop
// =================================================================
let currentOpenSide = null;

/* --- TOGGLES (Botones de abrir/cerrar) --- */

function toggleSidebarLeft() {
    if (currentOpenSide === 'left') {
        closeSidebarLeft();
    } else {
        closeSidebarRight(); // Cerramos el derecho por si estaba abierto

        const sidebar = document.getElementById('mobile-sidebar');
        if (sidebar) {
            sidebar.classList.add('is-open');
            openBackdrop('left');
        }
    }
}

function toggleSidebarRight() {
    if (currentOpenSide === 'right') {
        closeSidebarRight();
    } else {
        closeSidebarLeft(); // Cerramos el izquierdo por si estaba abierto

        const sidebarRight = document.getElementById('mobile-sidebar-right');
        if (sidebarRight) {
            sidebarRight.classList.add('is-open');
            openBackdrop('right');

            // TRUCO MÁGICO PARA EL MAPA
            setTimeout(function() {
                if (window.MetaMapa && window.MetaMapa['explore-map'] && window.MetaMapa['explore-map'].map) {
                    window.MetaMapa['explore-map'].map.invalidateSize();
                }
            }, 300);
        }
    }
}

/* --- FUNCIONES DE CIERRE SEGURAS --- */

function closeSidebarLeft() {
    const sidebar = document.getElementById('mobile-sidebar');
    if (sidebar) {
        sidebar.classList.remove('is-open');
    }
    hideBackdrop();
}

function closeSidebarRight() {
    const sidebarRight = document.getElementById('mobile-sidebar-right');
    if (sidebarRight) {
        sidebarRight.classList.remove('is-open');
    }
    hideBackdrop();
}

// Esta es la que usa el fondo negro (backdrop) al hacerle clic
function closeAllSidebars() {
    closeSidebarLeft();
    closeSidebarRight();
}

/* --- CONTROL DEL FONDO OSCURO --- */

function openBackdrop(side) {
    currentOpenSide = side;
    const backdrop = document.getElementById('sidebar-backdrop');
    if (backdrop) {
        backdrop.classList.add('is-open');
        document.body.style.overflow = 'hidden'; // Bloquea el scroll de atrás
    }
}

function hideBackdrop() {
    currentOpenSide = null;
    const backdrop = document.getElementById('sidebar-backdrop');
    if (backdrop) {
        backdrop.classList.remove('is-open');
        document.body.style.overflow = ''; // Restaura el scroll de atrás
    }
}