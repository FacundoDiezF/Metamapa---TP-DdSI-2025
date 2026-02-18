// =================================================================
// Cajon: mobile sidebar toggle y backdrop
// =================================================================
let currentOpenSide = null;

function toggleSidebarLeft() {
    document.getElementById('mobile-sidebar').classList.add('is-open');
    openBackdrop('left');
}

function toggleSidebarRight() {
    document.getElementById('mobile-sidebar-right').classList.add('is-open');
    openBackdrop('right');

    // TRUCO MÁGICO PARA EL MAPA:
    setTimeout(function() {
        if (window.MetaMapa && window.MetaMapa['explore-map'] && window.MetaMapa['explore-map'].map) {
            window.MetaMapa['explore-map'].map.invalidateSize();
        }
    }, 300);
}

function openBackdrop(side) {
    currentOpenSide = side;
    document.getElementById('sidebar-backdrop').classList.add('is-open');
    document.body.style.overflow = 'hidden'; // Bloquea el scroll de atrás
}

function closeAllSidebars() {
    // Cerramos ambos cajones por si las moscas
    document.getElementById('mobile-sidebar').classList.remove('is-open');
    document.getElementById('mobile-sidebar-right').classList.remove('is-open');

    // Apagamos el fondo negro
    document.getElementById('sidebar-backdrop').classList.remove('is-open');
    document.body.style.overflow = ''; // Restauramos el scroll
    currentOpenSide = null;
}