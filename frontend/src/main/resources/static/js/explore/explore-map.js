(function(){
    // Crear mapa
    const mapEl = document.getElementById('map');
    if (!mapEl || typeof L === 'undefined') return;

    const map = L.map(mapEl).setView([-34.6037, -58.3816], 11); // Buenos Aires por defecto
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        attribution: '&copy; OpenStreetMap'
    }).addTo(map);

    // Markers desde el backend
    const markers = (Array.isArray(MM_MARKERS) ? MM_MARKERS : []);
    const group = L.featureGroup();
    markers.forEach(m => {
        if (m.lat == null || m.lng == null) return;
        const marker = L.marker([m.lat, m.lng]).bindPopup(`
      <strong>${m.title ?? 'Hecho'}</strong><br/>
      <small>${m.category ?? ''}</small>
    `);
        group.addLayer(marker);
    });
    if (group.getLayers().length) {
        group.addTo(map);
        map.fitBounds(group.getBounds().pad(0.15));
    }

    // Hook desde el feed
    window.MM_zoomTo = (btn) => {
        const lat = parseFloat(btn.dataset.lat);
        const lng = parseFloat(btn.dataset.lng);
        if (Number.isFinite(lat) && Number.isFinite(lng)) {
            map.setView([lat, lng], 15);
        }
    };
})();