document.addEventListener('DOMContentLoaded', function() {
    const curatedBtn = document.getElementById('curatedModeBtn');
    const tooltip = document.getElementById('curatedModeTooltip');
    const tooltipText = tooltip?.querySelector('.tooltip-text');

    if (!curatedBtn || !tooltip) return;

    // Cargar estado previo desde localStorage
    const isCurated = localStorage.getItem('curatedMode') === 'true';
    if (isCurated) {
        curatedBtn.classList.add('active');
    }

    // Función para mostrar el tooltip
    function showTooltip(text, duration = 2000) {
        if (!tooltip || !tooltipText) return;

        tooltipText.textContent = text;
        tooltip.hidden = false;
        tooltip.classList.add('show');

        // Ocultar después de un tiempo
        setTimeout(() => {
            tooltip.classList.remove('show');
            setTimeout(() => {
                tooltip.hidden = true;
            }, 300); // Esperar a que termine la animación
        }, duration);
    }

    // Toggle al hacer clic
    curatedBtn.addEventListener('click', function() {
        const isActive = curatedBtn.classList.toggle('active');
        localStorage.setItem('curatedMode', isActive);

        // Mostrar tooltip con el modo actual
        const modeText = isActive ? 'Modo Curado activado' : 'Modo Curado desactivado';
        showTooltip(modeText);

        // Disparar evento personalizado para que otras partes de la app puedan reaccionar
        const event = new CustomEvent('curatedModeChange', {
            detail: { active: isActive }
        });
        document.dispatchEvent(event);

        console.log('Modo curado:', isActive ? 'activado' : 'desactivado');
    });
});

// Función helper para verificar si el modo curado está activo
function isCuratedModeActive() {
    return localStorage.getItem('curatedMode') === 'true';
}