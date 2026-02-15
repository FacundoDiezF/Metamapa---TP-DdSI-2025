// /js/metamapa-map.js
(function () {
    if (typeof L === 'undefined') return;

    const ICON_PALETTE = ["#2563eb", "#16a34a", "#ea580c", "#7c3aed", "#db2777", "#0891b2", "#ca8a04", "#ef4444", "#0ea5e9", "#10b981", "#f97316", "#ec4899", "#14b8a6", "#a855f7", "#84cc16", "#f59e0b", "#3b82f6", "#e11d48", "#8b5cf6", "#06b6d4", "#1e40af", "#15803d", "#b45309", "#5b21b6", "#9d174d", "#0e7490", "#854d0e", "#b91c1c", "#0369a1", "#047857", "#c2410c", "#6d28d9", "#be185d", "#0f766e", "#7e22ce", "#9a3412", "#1d4ed8", "#15803d", "#c026d3", "#ea580c", "#60a5fa", "#34d399", "#facc15", "#a78bfa", "#fb7185", "#67e8f9", "#bef264", "#fde047", "#93c5fd", "#fca5a5", "#99f6e4", "#86efac", "#fcd34d", "#c4b5fd", "#f9a8d4", "#22d3ee", "#bbf7d0", "#fef08a", "#ddd6fe", "#f5d0fe", "#bae6fd", "#a7f3d0", "#fde68a", "#e9d5ff", "#fecdd3", "#7dd3fc", "#6ee7b7", "#fef9c3", "#d8b4fe", "#fda4af", "#38bdf8", "#4ade80", "#fde68a", "#c084fc", "#fb7185", "#0ea5e9", "#22c55e", "#eab308", "#8b5cf6", "#f43f5e", "#0284c7", "#16a34a", "#ca8a04", "#7c3aed", "#e11d48"];

    function hashIdx(str) {
        let h = 5381;
        for (let i = 0; i < str.length; i++) h = ((h << 5) + h) + str.charCodeAt(i);
        return Math.abs(h) % ICON_PALETTE.length;
    }

    function makePinSVG(color) {
        return `
      <svg width="28" height="36" viewBox="0 0 28 36" xmlns="http://www.w3.org/2000/svg">
        <defs><filter id="shadow" x="-50%" y="-50%" width="200%" height="200%">
          <feDropShadow dx="0" dy="1.5" stdDeviation="1.5" flood-opacity="0.25"/></filter>
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

    function normalizeFromModel(ev) {
        if (!ev) return null;
        const lat = Number(ev.location?.latitude);
        const lng = Number(ev.location?.longitude);
        const ubicacion = (isFinite(lat) && isFinite(lng)) ? {lat, lng} : null;
        const fechaISO = ev.eventDate ? new Date(`${ev.eventDate}T00:00:00`).toISOString() : null;

        return {
            id: ev.id ?? null,
            titulo: ev.title ?? "(sin título)",
            categoria: ev.category ?? "Sin categoría",
            descripcion: ev.description ?? "",
            fechaISO,
            ubicacion,
            lugar: ev.location?.province ?? "",
            link: `/facts/${ev.id}`
        };
    }

    function fmt(iso) {
        return new Date(iso).toLocaleDateString("es-AR", {day: "2-digit", month: "2-digit", year: "numeric"});
    }

    function popupHtml(h) {
        return `
      <h4>${h.titulo}</h4>
      <div class="meta">${h.categoria ?? "–"} • ${h.fechaISO ? fmt(h.fechaISO) : "–"}</div>
      <div class="meta">${h.lugar ?? ""}</div>
    `;
    }

    // API pública
    window.initMetaMapa = function initMetaMapa({
                                                    containerId = 'map',
                                                    center = [-34.6037, -58.3816],
                                                    zoom = 11,
                                                    mini = false
                                                } = {}) {
        const isMiniMap = mini;
        const el = document.getElementById(containerId);
        if (!el) return;

        // Limpieza de instancias previas
        if (window.MetaMapa && window.MetaMapa[containerId] && window.MetaMapa[containerId].map) {
            window.MetaMapa[containerId].map.remove();
        }
        if (el._leaflet_id) {
            el._leaflet_id = null;
        }

        // Si está en una tarjeta/columna sin alto, dale un mínimo
        const frame = el.closest('.map-frame') || el.parentElement;
        if (frame && frame.getBoundingClientRect().height < 10) frame.style.height = '300px';

        const map = L.map(el, {zoomControl: true, attributionControl: true, scrollWheelZoom: true});
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19, attribution: '&copy; OpenStreetMap'
        }).addTo(map);
        map.setView(center, zoom);

        // Autoinvalidate en cambios de layout
        if ('ResizeObserver' in window) new ResizeObserver(() => map.invalidateSize()).observe(frame || el);
        window.addEventListener('load', () => map.invalidateSize());
        window.addEventListener('resize', () => map.invalidateSize());

        const markersLayer = L.layerGroup().addTo(map);

        function render(list) {
            markersLayer.clearLayers();
            list.forEach((h) => {
                if (!h?.ubicacion || !isFinite(h.ubicacion.lat) || !isFinite(h.ubicacion.lng)) return;

                const marker = L.marker([h.ubicacion.lat, h.ubicacion.lng], {
                    icon: getIconFor(h.categoria)
                }).addTo(markersLayer);

                if (isMiniMap) {
                    marker.on('click', () => {
                        const target = document.querySelector(`[data-event-id="${h.id}"]`);
                        if (target) {
                            target.scrollIntoView({behavior: 'smooth', block: 'center'});
                            target.classList.add('highlighted');
                            setTimeout(() => target.classList.remove('highlighted'), 1000);
                        } else {
                            console.warn(`No se encontró la card del evento con id ${h.id}`);
                        }
                    });
                } else {
                    marker.bindPopup(popupHtml(h), {closeButton: true, maxWidth: 320});
                }
            });
        }

        function update(rawList) {
            const evs = Array.isArray(rawList) ? rawList.map(normalizeFromModel) : [];
            render(evs);
        }

        // Toma los eventos del modelo si existen
        try {
            const evs = Array.isArray(window.events) ? window.events.map(normalizeFromModel) : [];
            render(evs);

        } catch (e) {
            console.error('No se pudieron renderizar los hechos', e);
        }

        // Exponer para debug puntual
        window.MetaMapa = window.MetaMapa || {};
        window.MetaMapa[containerId] = {map, markersLayer, render, update};
    };
})();
