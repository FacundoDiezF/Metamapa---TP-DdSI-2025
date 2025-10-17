(function () {
    if (typeof L === 'undefined') return; // Leaflet ya lo carga el layout

    // Acepta ambos IDs por si tu contenedor se llama #map o #explore-map
    const el = document.getElementById('explore-map') || document.getElementById('map');
    if (!el) return;

    // Si el contenedor no tiene alto, respetá el CSS; si no, damos un mínimo razonable
    const frame = el.closest('.map-frame');
    if (frame && frame.getBoundingClientRect().height < 10) frame.style.height = '300px';

    const map = L.map(el, {
        zoomControl: true,
        attributionControl: true,
        scrollWheelZoom: false
    });

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);

    // CABA por defecto
    map.setView([-34.6037, -58.3816], 11);

    // Recalcular tamaño si la columna cambia (collapse/tab)
    const mmMapa = document.getElementById('mm-mapa');
    if (mmMapa && 'ResizeObserver' in window) {
        new ResizeObserver(() => map.invalidateSize()).observe(mmMapa);
    }
    document.addEventListener('shown.bs.collapse', () => map.invalidateSize());
    document.addEventListener('shown.bs.tab', () => map.invalidateSize());

    // Exponer si hace falta depurar
    window.MetaMapa = window.MetaMapa || {};
    window.MetaMapa.exploreMap = map;
})();
