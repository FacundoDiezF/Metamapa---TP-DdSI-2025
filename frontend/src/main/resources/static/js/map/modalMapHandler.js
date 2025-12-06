(function() {
    // 1. Inicializar mapa (Coordenadas default: Buenos Aires, ajustalo a tu preferencia)
    var map = L.map('map-container').setView([-34.6037, -58.3816], 12);

    // 2. Cargar tiles (OpenStreetMap)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    // 3. Variable para el marcador
    var currentMarker = null;

    // 4. Evento click en el mapa
    map.on('click', function(e) {
        var lat = e.latlng.lat;
        var lng = e.latlng.lng;

        // Mover o crear marcador
        if (currentMarker) {
            currentMarker.setLatLng(e.latlng);
        } else {
            currentMarker = L.marker(e.latlng).addTo(map);
        }

        // Actualizar inputs ocultos
        document.getElementById('modal-lat').value = lat;
        document.getElementById('modal-lng').value = lng;

        // Habilitar botón confirmar
        document.getElementById('btn-confirm-loc').disabled = false;
    });

    // 5. Fix para renderizado de Leaflet dentro de modales ocultas/dinámicas
    setTimeout(function(){ map.invalidateSize(); }, 100);
})();