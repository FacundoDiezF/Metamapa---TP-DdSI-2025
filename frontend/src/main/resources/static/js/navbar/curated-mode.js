document.addEventListener('DOMContentLoaded', function () {
    const curatedBtn = document.getElementById('curatedModeBtn');
    const tooltip = document.getElementById('curatedModeTooltip');
    const tooltipText = tooltip?.querySelector('.tooltip-text');

    if (!curatedBtn || !tooltip) return;

    // 1. LEER ESTADO INICIAL DESDE FILTERMANAGER
    const currentFilters = FilterManager.getFilters();
    const isCurated = currentFilters['curatedNavigation'] === 'true';

    if (isCurated) {
        curatedBtn.classList.add('active');
    }

    // Función para mostrar el tooltip
    function showTooltip(text, duration = 2000) {
        if (!tooltip || !tooltipText) return;

        tooltipText.textContent = text;
        tooltip.hidden = false;
        tooltip.classList.add('show');

        setTimeout(() => {
            tooltip.classList.remove('show');
            setTimeout(() => {
                tooltip.hidden = true;
            }, 300);
        }, duration);
    }

    // 2. TOGGLE AL HACER CLICK
    curatedBtn.addEventListener('click', function () {
        // Cambios visuales
        const isActive = curatedBtn.classList.toggle('active');
        const modeText = isActive ? 'Modo Curado activado' : 'Modo Curado desactivado';
        showTooltip(modeText);

        // Guardamos el estado y navegamos
        FilterManager.updateFilters({
            curatedNavigation: isActive ? 'true' : 'false'
        });
        FilterManager.navigateWithFilters();
    });
});