// Mapa Leaflet simple
document.addEventListener('DOMContentLoaded', function () {
  const center = [-31.744, -60.490]; // Paraná, Entre Ríos (3 decimales)

  const map = L.map('map', {
    zoomControl: true,
    scrollWheelZoom: true
  }).setView(center, 12);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(map);

  L.marker(center).addTo(map).bindPopup('Paraná, Entre Ríos');
});
