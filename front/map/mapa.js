// --- Config inicial del mapa
const map = L.map('map', { zoomControl: true }).setView([-34.6037, -58.3816], 12);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap'
}).addTo(map);

// --- Datos de ejemplo (reemplazá por tu fetch a la API)
const hechos = [
    {
        id: 18754,
        titulo: 'Incendio forestal en Parque Nacional Nahuel Huapi',
        categoria: 'Desastre Natural',
        fechaISO: '2025-03-10T14:30:00Z',
        ubicacion: { lat: -41.135, lng: -71.310 },
        lugar: 'Parque Nacional Nahuel Huapi, Río Negro',
        autor: 'Ana García',
        link: '#'
    },
    {
        id: 19012,
        titulo: 'Contaminación en Río Reconquista',
        categoria: 'Medio Ambiente',
        fechaISO: '2025-03-09T09:10:00Z',
        ubicacion: { lat: -34.545, lng: -58.563 },
        lugar: 'Río Reconquista, San Martín, Buenos Aires',
        autor: 'Carlos Méndez',
        link: '#'
    }
];

// --- iconitos simples por categoría (podés sustituir por tus SVG de icons.css)
const iconByCat = {
    'Desastre Natural': L.divIcon({
        className: 'mm-icon',
        html: '🔴', iconSize: [24,24], iconAnchor:[12,12]
    }),
    'Medio Ambiente': L.divIcon({
        className: 'mm-icon',
        html: '🟢', iconSize: [24,24], iconAnchor:[12,12]
    })
};

// capa para poder refrescar marcadores al filtrar
let markersLayer = L.layerGroup().addTo(map);

// util
const fmt = (iso) => new Date(iso).toLocaleDateString('es-AR',{day:'2-digit',month:'2-digit',year:'numeric'});

// crea el HTML del popup
function popupHtml(h){
    return `
    <h4>${h.titulo}</h4>
    <div class="meta">${h.categoria} • ${fmt(h.fechaISO)}</div>
    <div class="meta">${h.lugar}</div>
    <a class="btn-link" href="${h.link}">Ver más detalles →</a>
  `;
}

// pinta marcadores según filtros actuales
function render(){
    markersLayer.clearLayers();

    // filtros
    const catsChecked = [...document.querySelectorAll('.chip input:checked')].map(i => i.value);
    const from = document.getElementById('dateFrom').value ? new Date(document.getElementById('dateFrom').value) : null;
    const to   = document.getElementById('dateTo').value ? new Date(document.getElementById('dateTo').value) : null;

    hechos
        .filter(h => catsChecked.includes(h.categoria))
        .filter(h => {
            const d = new Date(h.fechaISO);
            return (!from || d >= from) && (!to || d <= to);
        })
        .forEach(h => {
            const m = L.marker([h.ubicacion.lat, h.ubicacion.lng], {
                icon: iconByCat[h.categoria] || undefined
            })
                .bindPopup(popupHtml(h), { closeButton:true, maxWidth: 320 })
                .addTo(markersLayer);
        });
}

// eventos de UI
document.getElementById('applyFilters').addEventListener('click', render);
document.getElementById('clearFilters').addEventListener('click', () => {
    document.querySelectorAll('.chip input').forEach(i => i.checked = true);
    document.getElementById('dateFrom').value = '';
    document.getElementById('dateTo').value = '';
    render();
});

// geolocalización: centrar en mi ubicación
document.getElementById('centerOnMe').addEventListener('click', () => {
    if (!navigator.geolocation) return alert('Geolocalización no soportada');
    navigator.geolocation.getCurrentPosition(
        pos => {
            const { latitude, longitude } = pos.coords;
            map.setView([latitude, longitude], 14);
            // marcador suave de tu ubicación
            L.circleMarker([latitude, longitude], {
                radius: 7, color: '#3B82F6', weight: 2, fillColor: '#60a5fa', fillOpacity: .6
            }).addTo(map).bindPopup('Tu ubicación');
        },
        () => alert('No se pudo obtener tu ubicación'),
        { enableHighAccuracy: true, timeout: 8000 }
    );
});

// primera pintura
render();
