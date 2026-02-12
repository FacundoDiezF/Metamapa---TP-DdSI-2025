document.addEventListener('DOMContentLoaded', function() {

    // Función para mostrar toast de error
    function showToast(title, message) {
        const toastContainer = document.getElementById('toastContainer');

        if (!toastContainer) {
            console.error('No se encontró el contenedor de toast');
            return;
        }

        // Crear el toast
        const toast = document.createElement('div');
        toast.className = 'toast';
        toast.innerHTML = `
            <div class="toast-icon">!</div>
            <div class="toast-content">
                <h4 class="toast-title">${title}</h4>
                <p class="toast-message">${message}</p>
            </div>
            <button class="toast-close" aria-label="Cerrar notificación">×</button>
        `;

        toastContainer.appendChild(toast);

        // Animar entrada
        setTimeout(() => toast.classList.add('show'), 10);

        // Cerrar al hacer clic en la X
        const closeBtn = toast.querySelector('.toast-close');
        closeBtn.addEventListener('click', () => {
            toast.classList.add('hide');
            setTimeout(() => toast.remove(), 300);
        });

        // Auto-cerrar después de 5 segundos
        setTimeout(() => {
            if (toast.parentElement) {
                toast.classList.add('hide');
                setTimeout(() => toast.remove(), 300);
            }
        }, 5000);
    }

    // Obtener el input de archivo
    const fileInput = document.getElementById('csvfile');

    if (!fileInput) {
        console.error('No se encontró el input de archivo CSV');
        return;
    }

    // Validar cuando se selecciona un archivo
    fileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];

        if (file) {
            // Validar extensión
            const fileName = file.name.toLowerCase();
            const validExtensions = ['.csv'];
            const isValidExtension = validExtensions.some(ext => fileName.endsWith(ext));

            if (!isValidExtension) {
                // Mostrar error
                e.preventDefault();
                fileInput.value = ''; // Limpiar selección

                // Highlight temporal de error
                fileInput.classList.add('error');
                setTimeout(() => fileInput.classList.remove('error'), 3000);

                // Mostrar toast
                showToast(
                    'Archivo no válido',
                    `Solo se permiten archivos CSV. El archivo "${file.name}" no es un archivo CSV válido.`
                );

                return false;
            }

            // Validar tamaño (50MB)
            const maxSize = 50 * 1024 * 1024; // 50MB en bytes
            if (file.size > maxSize) {
                e.preventDefault();
                fileInput.value = '';

                fileInput.classList.add('error');
                setTimeout(() => fileInput.classList.remove('error'), 3000);

                showToast(
                    'Archivo demasiado grande',
                    `El archivo excede el tamaño máximo permitido de 50MB. Tamaño actual: ${(file.size / (1024 * 1024)).toFixed(2)}MB.`
                );

                return false;
            }

            // Si todo está bien, remover clase de error si existía
            fileInput.classList.remove('error');
        }
    });

    // Validar antes de enviar el formulario
    const form = fileInput.closest('form');

    if (form) {
        form.addEventListener('submit', function(e) {
            const file = fileInput.files[0];

            if (!file) {
                e.preventDefault();
                showToast(
                    'No hay archivo seleccionado',
                    'Por favor, selecciona un archivo CSV antes de iniciar la importación.'
                );
                return false;
            }

            const fileName = file.name.toLowerCase();
            if (!fileName.endsWith('.csv')) {
                e.preventDefault();
                showToast(
                    'Archivo no válido',
                    'Solo se permiten archivos CSV. Por favor, selecciona un archivo con extensión .csv'
                );
                return false;
            }
        });
    }
});

