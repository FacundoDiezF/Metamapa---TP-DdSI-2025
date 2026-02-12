// =================================================================
// FilterManager: Módulo de gestión de filtros con localStorage
// =================================================================
const LOCAL_STORAGE_KEY = 'appFilters';

const FilterManager = {

    // Función auxiliar para leer un formulario
    readForm(formElement) {
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
            newFilters = this.readForm(source);
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
        // 1. Empezamos con una copia COMPLETA de los filtros actuales
        const mergedFilters = {...currentFilters};

        let updates;
        if (source instanceof HTMLFormElement) {
            // Si viene de un formulario, lee solo las claves de ese formulario
            updates = this.readForm(source);
        } else if (typeof source === 'object' && source !== null) {
            updates = source;
        } else {
            console.error("updateFilters requiere un objeto o un elemento de formulario.");
            return;
        }

        // 2. Aplicamos las actualizaciones PARCIALES
        for (const key in updates) {
            const value = updates[key];

            // La clave 'key' solo se modificará o borrará si existe en 'updates'.
            if (value === null || value === undefined || value === '' || (Array.isArray(value) && value.length === 0)) {
                // Si el valor es nulo/vacío, borramos la clave del conjunto de filtros
                delete mergedFilters[key];
            } else {
                // Si tiene valor, actualizamos o añadimos la clave
                mergedFilters[key] = value;
            }
        }

        // 3. Guardamos el objeto fusionado (que aún contiene claves no modificadas como 'date_gt')
        this.setFilters(mergedFilters);
    },

    toggleArrayFilterValue(filterKey, valueToToggle, isChecked) {
        const currentFilters = this.getFilters();
        let currentValues = currentFilters[filterKey] || [];

        if (!Array.isArray(currentValues)) {
            currentValues = [currentValues];
        }

        const stringValue = String(valueToToggle);
        const index = currentValues.indexOf(stringValue);

        if (isChecked) {
            // Seleccionar: si no está, lo agregamos
            if (index === -1) {
                currentValues.push(stringValue);
            }
        } else {
            // Deseleccionar: si está, lo eliminamos
            if (index !== -1) {
                currentValues.splice(index, 1);
            }
        }

        // Preparamos el update para updateFilters
        const updates = {};
        updates[filterKey] = currentValues.length > 0 ? currentValues : null;

        // Actualizamos el conjunto de filtros en localStorage
        this.updateFilters(updates);
    },

    navigateWithFilters() {
        const finalFilters = this.getFilters();

        // 1. Construye la cadena de consulta (?...)
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

        // 2. Determina la URL base (pathname limpio)
        const currentUrl = new URL(window.location.href);
        const baseUrl = currentUrl.origin + currentUrl.pathname;

        // 3. Navega a la URL completa con los filtros
        window.location.href = baseUrl + newQueryString;
    },

    clearAndNavigate() {
        localStorage.removeItem(LOCAL_STORAGE_KEY);

        const currentUrl = new URL(window.location.href);
        currentUrl.search = '';
        currentUrl.hash = '';

        window.location.href = currentUrl.href;
    },
};
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

window.initializeModalChips = function (filtKey) {
    const filters = FilterManager.getFilters();
    let selectedIds = filters[filtKey] || [];

    if (!Array.isArray(selectedIds)) {
        selectedIds = [selectedIds];
    }
    selectedIds = selectedIds.map(String);

    const checkboxes = document.querySelectorAll('#chips-container input[type="checkbox"]');

    checkboxes.forEach(checkbox => {
        const categoryId = checkbox.value;

        checkbox.checked = selectedIds.includes(categoryId);
    });
};

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


// =================================================================
// Modal coleccion: Condiciones dinámicas, añadir/quitar filas y reindexar
// =================================================================function removeAndReindex(button) {

function removeAndReindex(button) {
    // Eliminar la fila
    button.closest('.condition-item').remove();

    // Reindexar
    document.querySelectorAll('#conditions-list .condition-item').forEach((row, index) => {
        row.querySelectorAll('[name^="conditions["]').forEach(input => {
            input.name = input.name.replace(/conditions\[\d+\]/, `conditions[${index}]`);
        });
    });
}
