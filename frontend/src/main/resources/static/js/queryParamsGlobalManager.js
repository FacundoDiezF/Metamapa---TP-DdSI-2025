// =================================================================
// FilterManager: Módulo de gestión de filtros con localStorage
// =================================================================
const LOCAL_STORAGE_KEY = 'appFilters';

const FilterManager = {

    // Función auxiliar para leer un formulario
    _readForm(formElement) {
        const formData = new FormData(formElement);
        const newFilters = {};

        for (const [key, value] of formData.entries()) {
            if (key.endsWith('_in')) {
                newFilters[key] = formData.getAll(key);
            } else {
                const stringValue = value.toString().trim();
                if (stringValue) {
                    newFilters[key] = stringValue;
                }
            }
        }
        return newFilters;
    },

    getFilters() {
        try {
            const filters = localStorage.getItem(LOCAL_STORAGE_KEY);
            return filters ? JSON.parse(filters) : {};
        } catch (e) {
            console.error("Error al leer localStorage:", e);
            return {};
        }
    },

    setFilters(source) {
        let newFilters;

        if (source instanceof HTMLFormElement) {
            newFilters = this._readForm(source);
        } else if (typeof source === 'object' && source !== null) {
            newFilters = source;
        } else {
            console.error("setFilters requiere un objeto o un elemento de formulario.");
            return;
        }

        try {
            const cleanedFilters = Object.fromEntries(
                Object.entries(newFilters).filter(([key, value]) =>
                    value !== undefined && value !== null && value !== '' &&
                    !(Array.isArray(value) && value.length === 0)
                )
            );

            localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(cleanedFilters));
        } catch (e) {
            console.error("Error al escribir en localStorage:", e);
        }
    },

    updateFilters(source) {
        const currentFilters = this.getFilters();
        const mergedFilters = { ...currentFilters };

        let updates;
        if (source instanceof HTMLFormElement) {
            updates = this.readForm(source); // LEE los datos del form como los updates
        } else if (typeof source === 'object' && source !== null) {
            updates = source;
        } else {
            console.error("updateFilters requiere un objeto o un elemento de formulario.");
            return;
        }

        // Aplica las actualizaciones al objeto mergedFilters
        for (const key in updates) {
            const value = updates[key];
            if (value === null || value === undefined || value === '') {
                delete mergedFilters[key];
            } else {
                mergedFilters[key] = value;
            }
        }

        // Finalmente, guarda el objeto fusionado
        this.setFilters(mergedFilters);
    },

    clearFilters() {
        localStorage.removeItem(LOCAL_STORAGE_KEY);
    },

    clearAndNavigate() {
        this.clearFilters();

        const currentUrl = new URL(window.location.href);
        currentUrl.search = '';
        currentUrl.hash = '';

        window.location.href = currentUrl.href;
    },

    updtaeAndNavigate(source) {
        this.updateFilters(source);

        const finalFilters = this.getFilters();

        const params = new URLSearchParams();
        for (const key in finalFilters) {
            const value = finalFilters[key];
            if (Array.isArray(value)) {
                value.forEach(item => params.append(key, item));
            } else if (value) {
                params.append(key, value);
            }
        }
        const newQueryString = params.toString() ? `?${params.toString()}` : '';

        const currentUrl = new URL(window.location.href);
        const baseUrl = currentUrl.origin + currentUrl.pathname;

        window.location.href = baseUrl + newQueryString;
    }
};

// Hacemos el manager accesible globalmente (para llamadas desde 'onsubmit'/'onclick')
window.FilterManager = FilterManager;

// =================================================================
// Lógica de Inicialización y Modificación de Enlaces
// =================================================================
document.addEventListener('DOMContentLoaded', () => {

    const savedFilters = FilterManager.getFilters();
    const hasSavedFilters = Object.keys(savedFilters).length > 0;

    // Convertimos los filtros guardados en una cadena de consulta
    let filterQueryString = '';
    if (hasSavedFilters) {
        const params = new URLSearchParams();
        for (const key in savedFilters) {
            const value = savedFilters[key];
            if (Array.isArray(value)) {
                value.forEach(item => params.append(key, item));
            } else if (value) {
                params.append(key, value);
            }
        }
        filterQueryString = params.toString() ? `?${params.toString()}` : '';
    }

    // 1. INYECTAR FILTROS EN ENLACES DE NAVEGACIÓN
    if (filterQueryString) {
        document.querySelectorAll('a').forEach(anchor => {
            const href = anchor.getAttribute('href');

            // Solo modificamos enlaces internos, sin filtros previos y que no sean botones de acción HTMX.
            if (href && href.startsWith('/') && !href.startsWith('#') && !href.includes('?') && !anchor.hasAttribute('hx-get')) {
                // EXCLUSIÓN: No queremos modificar el enlace de "Limpiar filtros"
                if (anchor.id !== 'clearFiltersBtn') {
                    anchor.setAttribute('href', `${href}${filterQueryString}`);
                }
            }
        });
    }
});

// =================================================================
// Solo estetcio: Guardar estado (abierto/cerrado) de las secciones de filtro
// =================================================================
document.querySelectorAll('details.flt').forEach(det => {
    const key = 'flt:' + det.querySelector('.flt-title').textContent.trim();
    const saved = localStorage.getItem(key);
    if (saved === 'open') det.setAttribute('open', '');

    det.addEventListener('toggle', () => {
        localStorage.setItem(key, det.open ? 'open' : 'closed');
    });
});

