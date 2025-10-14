/**
 * Manejo del modal de "Comparte tus hechos" (NUEVO EVENTO)
 */
document.addEventListener('DOMContentLoaded', function() {
    const shareDetails = document.querySelector('.share');
    const shareSummary = document.getElementById('shareSummary');
    const shareInput = shareSummary ? shareSummary.querySelector('input[readonly]') : null;

    if (!shareDetails || !shareSummary) {
        return; // no está en esta página
    }

    // Prevenir comportamiento por defecto del details/summary
    shareSummary.addEventListener('click', function(e) {
        e.preventDefault();

        // Toggle del atributo open
        if (shareDetails.hasAttribute('open')) {
            shareDetails.removeAttribute('open');
        } else {
            shareDetails.setAttribute('open', '');
        }
    });

    // Asegurar que el input no interfiera con el click
    if (shareInput) {
        shareInput.addEventListener('click', function(e) {
            e.stopPropagation();
            // Simular click en el summary padre
            shareSummary.click();
        });
    }

    // Opcional: cerrar al hacer click fuera
    document.addEventListener('click', function(e) {
        if (shareDetails.hasAttribute('open')) {
            const clickedInside = shareDetails.contains(e.target);
            if (!clickedInside) {
                shareDetails.removeAttribute('open');
            }
        }
    });

    // Opcional: cerrar con tecla Escape
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && shareDetails.hasAttribute('open')) {
            shareDetails.removeAttribute('open');
        }
    });
});

