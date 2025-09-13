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
    {
        id: 18754,
        titulo: "Incendio forestal en Parque Nacional Nahuel Huapi",
        categoria: "Desastre Natural",
        fechaISO: "2025-03-10T14:30:00Z",
        ubicacion: { lat: -41.135, lng: -71.31 },
        lugar: "Parque Nacional Nahuel Huapi, Río Negro",
        autor: "Ana García",
        link: "#",
    },
    {
        id: 19012,
        titulo: "Contaminación en Río Reconquista",
        categoria: "Medio Ambiente",
        fechaISO: "2025-03-09T09:10:00Z",
        ubicacion: { lat: -34.545, lng: -58.563 },
        lugar: "Río Reconquista, San Martín, Buenos Aires",
        autor: "Carlos Méndez",
        link: "#",
    },
];

// ======================
/** Iconos simples por categoría (podés cambiarlos por tus SVG) */
const iconByCat = {
    "Desastre Natural": L.divIcon({
        className: "mm-icon",
        html: "🔴",
        iconSize: [24, 24],
        iconAnchor: [12, 12],
    }),
    "Medio Ambiente": L.divIcon({
        className: "mm-icon",
        html: "🟢",
        iconSize: [24, 24],
        iconAnchor: [12, 12],
    }),
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
