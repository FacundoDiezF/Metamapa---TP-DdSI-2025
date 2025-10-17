
const map = L.map("map", { zoomControl: true }).setView([-34.6037, -58.3816], 12);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "&copy; OpenStreetMap",
}).addTo(map);

console.log("eventos desde Thymeleaf:", window.events);



const wrap = document.querySelector('.map-wrap') || document.getElementById('map')?.parentElement;
if (wrap && 'ResizeObserver' in window) {
    const ro = new ResizeObserver(() => map.invalidateSize());
    ro.observe(wrap);
}


window.addEventListener("load", () => map.invalidateSize());
window.addEventListener("resize", () => map.invalidateSize());
document.addEventListener("click", (e) => {
    const sum = e.target.closest("summary");
    if (sum && sum.parentElement?.matches("details.flt")) {
        setTimeout(() => map.invalidateSize(), 180);
    }
});

let events = [];
const markersLayer = L.layerGroup().addTo(map);


// ===== Iconos dinámicos por categoría (cacheados) =====
const ICON_PALETTE = [
    "#2563eb", "#16a34a", "#ea580c", "#7c3aed", "#db2777",
    "#0891b2", "#ca8a04", "#ef4444", "#0ea5e9", "#10b981",
    "#f97316", "#ec4899", "#14b8a6", "#a855f7", "#84cc16",
    "#f59e0b", "#3b82f6", "#e11d48", "#8b5cf6", "#06b6d4",
    "#1e40af", "#15803d", "#b45309", "#5b21b6", "#9d174d",
    "#0e7490", "#854d0e", "#b91c1c", "#0369a1", "#047857",
    "#c2410c", "#6d28d9", "#be185d", "#0f766e", "#7e22ce",
    "#9a3412", "#1d4ed8", "#15803d", "#c026d3", "#ea580c",
    "#60a5fa", "#34d399", "#facc15", "#a78bfa", "#fb7185",
    "#67e8f9", "#bef264", "#fde047", "#93c5fd", "#fca5a5",
    "#99f6e4", "#86efac", "#fcd34d", "#c4b5fd", "#f9a8d4",
    "#22d3ee", "#bbf7d0", "#fef08a", "#ddd6fe", "#f5d0fe",
    "#bae6fd", "#a7f3d0", "#fde68a", "#e9d5ff", "#fecdd3",
    "#7dd3fc", "#6ee7b7", "#fef9c3", "#d8b4fe", "#fda4af",
    "#38bdf8", "#4ade80", "#fde68a", "#c084fc", "#fb7185",
    "#0ea5e9", "#22c55e", "#eab308", "#8b5cf6", "#f43f5e",
    "#0284c7", "#16a34a", "#ca8a04", "#7c3aed", "#e11d48"
];


// hash -> índice de la paleta (determinístico)
function hashIdx(str) {
    let h = 5381;
    for (let i = 0; i < str.length; i++) h = ((h << 5) + h) + str.charCodeAt(i);
    h = Math.abs(h);
    return h % ICON_PALETTE.length;
}


// Ícono SVG (pin simple del color correspondiente)
function makePinSVG(color) {
    return `
  <svg width="28" height="36" viewBox="0 0 28 36" xmlns="http://www.w3.org/2000/svg">
    <defs>
      <filter id="shadow" x="-50%" y="-50%" width="200%" height="200%">
        <feDropShadow dx="0" dy="1.5" stdDeviation="1.5" flood-opacity="0.25"/>
      </filter>
    </defs>
    <path filter="url(#shadow)" fill="${color}"
      d="M14 0c7.18 0 13 5.59 13 12.49C27 22 14 36 14 36S1 22 1 12.49C1 5.59 6.82 0 14 0z"/>
    <circle cx="14" cy="12" r="6" fill="white"/>
  </svg>`;
}

const iconCache = new Map();
function getIconFor(category = "Sin categoría") {
    if (iconCache.has(category)) return iconCache.get(category);
    const color = ICON_PALETTE[hashIdx(category)];
    const icon = L.divIcon({
        className: "mm-icon",
        html: makePinSVG(color),
        iconSize: [28, 36],
        iconAnchor: [14, 34],
        popupAnchor: [0, -30]
    });
    iconCache.set(category, icon);
    return icon;
}


// ======================
//  Helpers UI
// ======================
const $  = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

const fmt = (iso) =>
    new Date(iso).toLocaleDateString("es-AR", { day: "2-digit", month: "2-digit", year: "numeric" });

function popupHtml(h) {
    return `
    <h4>${h.titulo}</h4>
    <div class="meta">${h.categoria ?? "–"} • ${h.fechaISO ? fmt(h.fechaISO) : "–"}</div>
    <div class="meta">${h.lugar ?? ""}</div>
    ${h.link ? `<a class="btn-link" href="${h.link}">Ver más →</a>` : ""}
  `;
}

function getCheckedCategories() {
    let inputs = $$(".chip-toggle input:checked");
    if (!inputs.length) inputs = $$(".chip input:checked");
    return inputs.map(i => i.value);
}

function getDateRange() {
    const fromEl = $("#dateFrom");
    const toEl   = $("#dateTo");
    const from = fromEl?.value ? new Date(fromEl.value) : null;
    const to   = toEl?.value   ? new Date(toEl.value)   : null;
    return { from, to, fromEl, toEl };
}

// ======================
//  Normalización del DTO del modelo
//  (ajustá nombres si tu EventDTO difiere)
// ======================
function toISO(d) {
    if (!d) return null;
    if (typeof d === "string" && /\d{4}-\d{2}-\d{2}T/.test(d)) return d;
    const dt = new Date(d);
    return isNaN(dt) ? null : dt.toISOString();
}

function pickLatLng(ev) {
    const loc = ev.location || ev.ubicacion || ev.geo || ev.coords;
    if (loc && (("lat" in loc && "lng" in loc) || ("latitude" in loc && "longitude" in loc))) {
        return { lat: Number(loc.lat ?? loc.latitude), lng: Number(loc.lng ?? loc.longitude) };
    }
    if (("lat" in ev && "lng" in ev) || ("latitude" in ev && "longitude" in ev)) {
        return { lat: Number(ev.lat ?? ev.latitude), lng: Number(ev.lng ?? ev.longitude) };
    }
    return null;
}

function normalizeFromModel(ev) {
    if (!ev) return null;

    const lat = Number(ev.location?.latitude);
    const lng = Number(ev.location?.longitude);
    const ubicacion = (isFinite(lat) && isFinite(lng)) ? { lat, lng } : null;

    // eventDate viene como LocalDate ("YYYY-MM-DD"): lo paso a ISO de día local
    const fechaISO = ev.eventDate ? new Date(`${ev.eventDate}T00:00:00`).toISOString() : null;

    return {
        id: ev.id ?? null,
        titulo: ev.title ?? "(sin título)",
        categoria: ev.category ?? "Sin categoría",
        descripcion: ev.description ?? "",
        fechaISO,
        ubicacion,
        // muestro la provincia si viene
        lugar: ev.location?.province ?? "",
        link: `/facts/${ev.id}` // ajustá si tu ruta es otra
    };
}



//  Render
// ======================
function render(list = hechos) {
    markersLayer.clearLayers();

    list.forEach((h) => {
        if (!h?.ubicacion || !isFinite(h.ubicacion.lat) || !isFinite(h.ubicacion.lng)) return;

        const ic = getIconFor(h.categoria);

        L.marker([h.ubicacion.lat, h.ubicacion.lng],  { icon: ic })
            .bindPopup(popupHtml(h), { closeButton: true, maxWidth: 320 })
            .addTo(markersLayer);
    });
}


// ======================
//  Eventos UI
// ======================
$("#applyFilters")?.addEventListener("click", (e) => {
    e.preventDefault?.();
    render();
});

$("#clearFilters")?.addEventListener("click", (e) => {
    e.preventDefault?.();
    $$(".chip-toggle input, .chip input").forEach(i => (i.checked = false));
    const { fromEl, toEl } = getDateRange();
    if (fromEl) fromEl.value = "";
    if (toEl) toEl.value = "";
    render();
});

$("#centerOnMe")?.addEventListener("click", () => {
    if (!navigator.geolocation) return alert("Geolocalización no soportada");
    navigator.geolocation.getCurrentPosition(
        (pos) => {
            const { latitude, longitude } = pos.coords;
            map.setView([latitude, longitude], 14);
            L.circleMarker([latitude, longitude], {
                radius: 7, color: "#3B82F6", weight: 2, fillColor: "#60a5fa", fillOpacity: 0.6,
            }).addTo(map).bindPopup("Tu ubicación");
        },
        () => alert("No se pudo obtener tu ubicación"),
        { enableHighAccuracy: true, timeout: 8000 }
    );
});

// ======================
//  Primera carga
// ======================
(function init() {

    try {
        events = Array.isArray(window.events) ? window.events.map(normalizeFromModel) : [];
        render(events);
    } catch (err) {
        console.error("Error inicial cargando eventos:", err);
        alert("No se pudieron renderizar los hechos.");
    }
})();
