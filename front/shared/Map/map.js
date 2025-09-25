// shared/map.js
document.addEventListener('DOMContentLoaded', () => {
  // Busca mapas por data-attr o por id #map (compat con index actual)
  const candidates = document.querySelectorAll('[data-mm-map], #map');
  if (!candidates.length) return;

  candidates.forEach((el) => {
    if (el.dataset.mmInited === 'true') return; // evita doble init
    el.dataset.mmInited = 'true';

    // Defaults: Paraná (como en index)
    const lat  = parseFloat(el.dataset.centerLat ?? '-31.744');
    const lng  = parseFloat(el.dataset.centerLng ?? '-60.490');
    const zoom = parseInt(el.dataset.zoom ?? '12', 10);

    const map = L.map(el, {
      zoomControl: true,
      scrollWheelZoom: true,
    }).setView([lat, lng], zoom);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    // Marcador opcional (por defecto ON)
    if (el.dataset.marker !== 'false') {
      const popupText = el.dataset.popup || 'Paraná, Entre Ríos';
      L.marker([lat, lng]).addTo(map).bindPopup(popupText);
    }
  });
});
