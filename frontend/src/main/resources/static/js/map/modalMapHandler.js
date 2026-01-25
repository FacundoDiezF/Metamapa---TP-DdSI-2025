(function() {
    var container = document.getElementById('map-container');
    if (!container) return; // Seguridad por si no cargó el HTML aún

    // 1. LIMPIEZA: Si ya existía un mapa previo en la ventana global, eliminarlo
    // Esto es vital en SPAs/HTMX para evitar el error "Map container is already initialized"
    if (window.modalMapInstance) {
        window.modalMapInstance.remove();
        window.modalMapInstance = null;
    }

    // 2. LEER DATOS: Obtener lat/lng iniciales (si venimos de edición)
    var initLat = container.getAttribute('data-lat');
    var initLng = container.getAttribute('data-lng');
    var hasLocation = (initLat && initLng);

    // Configuración inicial (Si hay datos, zoom cerca; si no, vista general BsAs)
    var startLat = hasLocation ? parseFloat(initLat) : -34.6037;
    var startLng = hasLocation ? parseFloat(initLng) : -58.3816;
    var startZoom = hasLocation ? 10 : 10;

    // 3. INICIALIZAR MAPA
    var map = L.map('map-container').setView([startLat, startLng], startZoom);

    // Guardamos la referencia global para poder limpiarlo la próxima vez
    window.modalMapInstance = map;

    // Cargar tiles
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    var currentMarker = null;

    // 4. SI ES EDICIÓN: Poner el marcador inicial
    if (hasLocation) {
        currentMarker = L.marker([startLat, startLng]).addTo(map);

        // Habilitar el botón inmediatamente porque ya es una ubicación válida
        var btn = document.getElementById('btn-confirm-loc');
        if(btn) btn.disabled = false;
    }

    // 5. EVENTO CLICK
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

    // 6. FIX RENDERIZADO (Vital para modales)
    // Leaflet necesita recalcular el tamaño del contenedor una vez que es visible
    setTimeout(function(){
        map.invalidateSize();

        // Si hay marcador inicial, panear suavemente para asegurar que esté centrado
        if(hasLocation) {
            map.panTo([startLat, startLng]);
        }
    }, 200);

})();