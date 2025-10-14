/**
 * Manejo del modal de "Comparte tus hechos" (NUEVO EVENTO)
 * VERSION CON CONTROL COMPLETO DE VISUALIZACION
 */
document.addEventListener('DOMContentLoaded', function() {
    const shareDetails = document.querySelector('.share');
    const shareSummary = document.getElementById('shareSummary');
    const sharePanel = document.querySelector('.share-panel');
    const shareInput = shareSummary ? shareSummary.querySelector('input[readonly]') : null;

    console.log('=== SHARE MODAL DEBUG ===');
    console.log('shareDetails:', shareDetails);
    console.log('shareSummary:', shareSummary);
    console.log('sharePanel:', sharePanel);

    if (!shareDetails || !shareSummary || !sharePanel) {
        console.error('❌ Elementos no encontrados');
        return;
    }

    console.log('✅ Todos los elementos encontrados');

    // Verificar estilos iniciales
    console.log('sharePanel display inicial:', window.getComputedStyle(sharePanel).display);

    // FORZAR estado inicial oculto
    sharePanel.style.display = 'none';
    console.log('🔒 Forzado a display: none inicial');

    // Manejar toggle del details
    shareDetails.addEventListener('toggle', function() {
        const isOpen = shareDetails.open;
        console.log(isOpen ? '📂 Intentando ABRIR modal' : '📁 Intentando CERRAR modal');

        if (isOpen) {
            // ABRIR: forzar display grid
            sharePanel.style.setProperty('display', 'grid', 'important');
            console.log('✅ FORZADO: sharePanel.style.display = grid (important)');

            // Verificar que se aplicó
            setTimeout(() => {
                const computedDisplay = window.getComputedStyle(sharePanel).display;
                console.log('🔍 Display después de forzar:', computedDisplay);
                console.log('🔍 sharePanel.offsetHeight:', sharePanel.offsetHeight);
                console.log('🔍 sharePanel visible:', sharePanel.offsetHeight > 0);
            }, 50);
        } else {
            // CERRAR: ocultar
            sharePanel.style.setProperty('display', 'none', 'important');
            console.log('✅ CERRADO: sharePanel.style.display = none (important)');
        }
    });

    // Cerrar con Escape
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && shareDetails.open) {
            shareDetails.removeAttribute('open');
            console.log('⌨️ Cerrado con Escape');
        }
    });

    console.log('=== INICIALIZADO ===');
});
