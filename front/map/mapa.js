// ======================
//  Configuración del mapa
// ======================
const map = L.map("map", { zoomControl: true }).setView([-34.6037, -58.3816], 12);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "&copy; OpenStreetMap",
}).addTo(map);

// Asegura que Leaflet calcule bien el tamaño cuando cambia el layout
window.addEventListener("load", () => map.invalidateSize());
window.addEventListener("resize", () => map.invalidateSize());
// Si abrís/cerrás los <details> de filtros, re-calcular tamaño
document.addEventListener("click", (e) => {
    const sum = e.target.closest("summary");
    if (sum && sum.parentElement?.matches("details.flt")) {
        setTimeout(() => map.invalidateSize(), 180);
    }
});

// ======================
//  Datos de ejemplo
// ======================
const hechos = [
    { id: 1, titulo: "Incendio forestal en Parque Nacional Nahuel Huapi", categoria: "Desastre Natural", fechaISO: "2025-03-10T14:30:00Z", ubicacion: { lat: -41.135, lng: -71.31 }, lugar: "Parque Nacional Nahuel Huapi, Río Negro", autor: "Ana García", link: "#" },
    { id: 2, titulo: "Contaminación en Río Reconquista", categoria: "Medio Ambiente", fechaISO: "2025-03-09T09:10:00Z", ubicacion: { lat: -34.545, lng: -58.563 }, lugar: "Río Reconquista, San Martín, Buenos Aires", autor: "Carlos Méndez", link: "#" },
    { id: 3, titulo: "Inundación en Yakarta", categoria: "Desastre Natural", fechaISO: "2025-02-15T08:00:00Z", ubicacion: { lat: -6.2088, lng: 106.8456 }, lugar: "Yakarta, Indonesia", autor: "Agus Santoso", link: "#" },
    { id: 4, titulo: "Protesta por derechos humanos en Hong Kong", categoria: "Derechos Humanos", fechaISO: "2025-02-20T12:00:00Z", ubicacion: { lat: 22.3193, lng: 114.1694 }, lugar: "Hong Kong", autor: "Li Wei", link: "#" },
    { id: 5, titulo: "Derrame de petróleo en Golfo de México", categoria: "Medio Ambiente", fechaISO: "2025-01-18T15:30:00Z", ubicacion: { lat: 25.0, lng: -90.0 }, lugar: "Golfo de México", autor: "John Smith", link: "#" },
    { id: 6, titulo: "Terremoto en Ciudad de México", categoria: "Desastre Natural", fechaISO: "2025-02-11T03:45:00Z", ubicacion: { lat: 19.4326, lng: -99.1332 }, lugar: "Ciudad de México, México", autor: "María López", link: "#" },
    { id: 7, titulo: "Manifestación ambiental en Berlín", categoria: "Medio Ambiente", fechaISO: "2025-01-22T14:00:00Z", ubicacion: { lat: 52.52, lng: 13.405 }, lugar: "Berlín, Alemania", autor: "Hans Müller", link: "#" },
    { id: 8, titulo: "Deshielo récord en Groenlandia", categoria: "Medio Ambiente", fechaISO: "2025-03-01T11:00:00Z", ubicacion: { lat: 71.7069, lng: -42.6043 }, lugar: "Groenlandia", autor: "Inge Sørensen", link: "#" },
    { id: 9, titulo: "Conflicto social en Lima", categoria: "Derechos Humanos", fechaISO: "2025-02-05T09:20:00Z", ubicacion: { lat: -12.0464, lng: -77.0428 }, lugar: "Lima, Perú", autor: "Pedro Quispe", link: "#" },
    { id: 10, titulo: "Huracán en Miami", categoria: "Desastre Natural", fechaISO: "2025-09-01T06:30:00Z", ubicacion: { lat: 25.7617, lng: -80.1918 }, lugar: "Miami, EE.UU.", autor: "Emily Johnson", link: "#" },
    { id: 11, titulo: "Contaminación plástica en Manila", categoria: "Medio Ambiente", fechaISO: "2025-07-22T12:00:00Z", ubicacion: { lat: 14.5995, lng: 120.9842 }, lugar: "Manila, Filipinas", autor: "Josefina Cruz", link: "#" },
    { id: 12, titulo: "Crisis humanitaria en Siria", categoria: "Derechos Humanos", fechaISO: "2025-06-15T10:15:00Z", ubicacion: { lat: 34.8021, lng: 38.9968 }, lugar: "Siria", autor: "Omar Al-Fayed", link: "#" },
    { id: 13, titulo: "Volcán Etna en erupción", categoria: "Desastre Natural", fechaISO: "2025-05-18T18:00:00Z", ubicacion: { lat: 37.7510, lng: 14.9934 }, lugar: "Sicilia, Italia", autor: "Giuseppe Romano", link: "#" },
    { id: 14, titulo: "Deforestación en Amazonía", categoria: "Medio Ambiente", fechaISO: "2025-04-10T13:00:00Z", ubicacion: { lat: -3.4653, lng: -62.2159 }, lugar: "Amazonas, Brasil", autor: "Fernanda Silva", link: "#" },
    { id: 15, titulo: "Protesta por libertad de prensa en Moscú", categoria: "Derechos Humanos", fechaISO: "2025-02-25T16:00:00Z", ubicacion: { lat: 55.7558, lng: 37.6173 }, lugar: "Moscú, Rusia", autor: "Ivan Petrov", link: "#" },
    { id: 16, titulo: "Aluvión en Cusco", categoria: "Desastre Natural", fechaISO: "2025-01-30T07:00:00Z", ubicacion: { lat: -13.5319, lng: -71.9675 }, lugar: "Cusco, Perú", autor: "Rosa Huamán", link: "#" },
    { id: 17, titulo: "Energía solar masiva en Sahara", categoria: "Medio Ambiente", fechaISO: "2025-02-02T12:00:00Z", ubicacion: { lat: 23.4162, lng: 25.6628 }, lugar: "Desierto del Sahara", autor: "Ahmed Ben Ali", link: "#" },
    { id: 18, titulo: "Marcha feminista en Madrid", categoria: "Derechos Humanos", fechaISO: "2025-03-08T18:00:00Z", ubicacion: { lat: 40.4168, lng: -3.7038 }, lugar: "Madrid, España", autor: "Laura González", link: "#" },
    { id: 19, titulo: "Sequía extrema en Ciudad del Cabo", categoria: "Medio Ambiente", fechaISO: "2025-03-21T09:00:00Z", ubicacion: { lat: -33.9249, lng: 18.4241 }, lugar: "Ciudad del Cabo, Sudáfrica", autor: "Naledi Dlamini", link: "#" },
    { id: 20, titulo: "Nevada histórica en Nueva York", categoria: "Desastre Natural", fechaISO: "2025-01-05T06:00:00Z", ubicacion: { lat: 40.7128, lng: -74.0060 }, lugar: "Nueva York, EE.UU.", autor: "Michael Brown", link: "#" },
    { id: 21, titulo: "Conflicto indígena en Chiapas", categoria: "Derechos Humanos", fechaISO: "2025-02-12T14:00:00Z", ubicacion: { lat: 16.7569, lng: -93.1292 }, lugar: "Chiapas, México", autor: "Juan Pérez", link: "#" },
    { id: 22, titulo: "Apareció un Charizard salvaje en Tokio", categoria: "Easter Egg", fechaISO: "2025-04-01T10:00:00Z", ubicacion: { lat: 35.6762, lng: 139.6503 }, lugar: "Tokio, Japón", autor: "Ash Ketchum", link: "#" },
    { id: 23, titulo: "Aurora boreal visible en Londres", categoria: "Easter Egg", fechaISO: "2025-03-17T22:00:00Z", ubicacion: { lat: 51.5074, lng: -0.1278 }, lugar: "Londres, Reino Unido", autor: "Profesor Oak", link: "#" },
    {id: 18754, titulo: "Incendio forestal en Parque Nacional Nahuel Huapi", categoria: "Desastre Natural", fechaISO: "2025-03-10T14:30:00Z", ubicacion: { lat: -41.135, lng: -71.31 }, lugar: "Parque Nacional Nahuel Huapi, Río Negro", autor: "Ana García", link: "#",},
    {id: 19012, titulo: "Contaminación en Río Reconquista", categoria: "Medio Ambiente", fechaISO: "2025-03-09T09:10:00Z", ubicacion: { lat: -34.545, lng: -58.563 }, lugar: "Río Reconquista, San Martín, Buenos Aires", autor: "Carlos Méndez", link: "#"},
    { id: 24, titulo: "Erupción del volcán Kilauea", categoria: "Desastre Natural", fechaISO: "2025-04-12T06:00:00Z", ubicacion: { lat: 19.4069, lng: -155.2834 }, lugar: "Hawái, EE.UU.", autor: "Keoni Kalani", link: "#" },
    { id: 25, titulo: "Vertido químico en río Rin", categoria: "Medio Ambiente", fechaISO: "2025-05-02T09:30:00Z", ubicacion: { lat: 50.1109, lng: 8.6821 }, lugar: "Fráncfort, Alemania", autor: "Greta Schmitt", link: "#" },
    { id: 26, titulo: "Marcha estudiantil por la educación", categoria: "Derechos Humanos", fechaISO: "2025-04-22T14:00:00Z", ubicacion: { lat: -33.4489, lng: -70.6693 }, lugar: "Santiago de Chile", autor: "Camila Torres", link: "#" },
    { id: 27, titulo: "Se avistó un OVNI en Nevada", categoria: "Easter Egg", fechaISO: "2025-07-04T23:00:00Z", ubicacion: { lat: 37.2431, lng: -115.7930 }, lugar: "Área 51, EE.UU.", autor: "Mulder & Scully", link: "#" },
    { id: 28, titulo: "El Obelisco amaneció vestido de Pikachu", categoria: "Easter Egg", fechaISO: "2025-08-15T08:00:00Z", ubicacion: { lat: -34.6037, lng: -58.3816 }, lugar: "Obelisco, Buenos Aires", autor: "Red Pikachu Team", link: "#" },

];

// ======================
/** Iconos simples por categoría (podés cambiarlos por tus SVG) */
const iconByCat = {
    "Desastre Natural": L.divIcon({ className: "mm-icon", html: "🔴", iconSize: [24, 24], iconAnchor: [12, 12] }),
    "Medio Ambiente":  L.divIcon({ className: "mm-icon", html: "🟢", iconSize: [24, 24], iconAnchor: [12, 12] }),
    "Derechos Humanos":L.divIcon({ className: "mm-icon", html: "🟣", iconSize: [24, 24], iconAnchor: [12, 12] }),
    "Easter Egg":      L.divIcon({ className: "mm-icon", html: "✨",  iconSize: [24, 24], iconAnchor: [12, 12] }),
};


// Capa para poder refrescar marcadores al filtrar
let markersLayer = L.layerGroup().addTo(map);

// Utils
const fmt = (iso) =>
    new Date(iso).toLocaleDateString("es-AR", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
    });

function popupHtml(h) {
    return `
    <h4>${h.titulo}</h4>
    <div class="meta">${h.categoria} • ${fmt(h.fechaISO)}</div>
    <div class="meta">${h.lugar}</div>
    <a class="btn-link" href="${h.link}">Ver más detalles →</a>
  `;
}

// ======================
//  Lectura de filtros (robusta a ambos markups)
// ======================
const $ = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

function getCheckedCategories() {
    // Nuevo markup (inputs ocultos dentro de .chip-toggle)
    let inputs = $$(".chip-toggle input:checked");
    if (!inputs.length) {
        // Compatibilidad con markup anterior del mapa
        inputs = $$(".chip input:checked");
    }
    if (inputs.length) return inputs.map((i) => i.value);

    // Si no hay inputs marcados, por seguridad devuelve todas las distintas categorías de los datos
    return [...new Set(hechos.map((h) => h.categoria))];
}

function getDateRange() {
    // Preferencia por #dateFrom / #dateTo si existen
    const fromEl = $("#dateFrom") || $('input[name="fecha_desde"]');
    const toEl = $("#dateTo") || $('input[name="fecha_hasta"]');

    const from = fromEl?.value ? new Date(fromEl.value) : null;
    const to = toEl?.value ? new Date(toEl.value) : null;
    return { from, to, fromEl, toEl };
}

// ======================
//  Pintado de marcadores
// ======================
function render() {
    markersLayer.clearLayers();

    const cats = getCheckedCategories();
    const { from, to } = getDateRange();

    hechos
        .filter((h) => cats.includes(h.categoria))
        .filter((h) => {
            const d = new Date(h.fechaISO);
            return (!from || d >= from) && (!to || d <= to);
        })
        .forEach((h) => {
            L.marker([h.ubicacion.lat, h.ubicacion.lng], {
                icon: iconByCat[h.categoria] || undefined,
            })
                .bindPopup(popupHtml(h), { closeButton: true, maxWidth: 320 })
                .addTo(markersLayer);
        });
}

// ======================
//  Eventos de UI
// ======================

// Botón Aplicar: soporta #applyFilters o .btn-apply
const btnApply =
    $("#applyFilters") || $(".btn-apply") || $('[type="submit"].btn-apply');
btnApply?.addEventListener("click", (e) => {
    e.preventDefault?.();
    render();
});

// Botón Limpiar: soporta #clearFilters o .btn-clean (si es <button type="reset"> igual lo forzamos)
const btnClear = $("#clearFilters") || $(".btn-clean");
btnClear?.addEventListener("click", (e) => {
    e.preventDefault?.(); // evitamos que un form cambie la URL
    // Desmarcar categorías (ambos markups)
    $$(".chip-toggle input, .chip input").forEach((i) => (i.checked = false));
    // Limpiar fechas
    const { fromEl, toEl } = getDateRange();
    if (fromEl) fromEl.value = "";
    if (toEl) toEl.value = "";
    render();
});

// Botón "Centrar en mi ubicación"
$("#centerOnMe")?.addEventListener("click", () => {
    if (!navigator.geolocation) {
        alert("Geolocalización no soportada");
        return;
    }
    navigator.geolocation.getCurrentPosition(
        (pos) => {
            const { latitude, longitude } = pos.coords;
            map.setView([latitude, longitude], 14);
            L.circleMarker([latitude, longitude], {
                radius: 7,
                color: "#3B82F6",
                weight: 2,
                fillColor: "#60a5fa",
                fillOpacity: 0.6,
            })
                .addTo(map)
                .bindPopup("Tu ubicación");
        },
        () => alert("No se pudo obtener tu ubicación"),
        { enableHighAccuracy: true, timeout: 8000 }
    );
});

// ======================
//  Primera pintura
// ======================
render();
