// front/Landing/auth.js
(function () {
  function $(id) { return document.getElementById(id); }

  // Refs del modal
  const overlay = $('authOverlay');
  const closeBtn = $('authClose');
  const loginPanel = $('loginPanel');
  const registerPanel = $('registerPanel');
  const toggleRegister = $('toggleRegister');
  const toggleLogin = $('toggleLogin');
  const loginForm = $('loginForm');
  const registerForm = $('registerForm');

  if (!overlay) return; // si no existe el modal, salgo

  // Helpers
  function show(panel) {
    const isReg = panel === 'register';
    if (loginPanel) loginPanel.hidden = isReg;
    if (registerPanel) registerPanel.hidden = !isReg;
  }
  function openModal(panel = 'login') {
    overlay.hidden = false;
    document.body.classList.add('modal-open');
    show(panel);
  }
  function closeModal() {
    overlay.hidden = true;
    document.body.classList.remove('modal-open');
  }

  // Exponer para uso opcional desde otros scripts
  window.openAuthModal = openModal;

  // Delegación: cualquier elemento con data-auth="open"
  // abre el modal, y `data-panel` define el panel inicial.
  document.addEventListener('click', (e) => {
    const el = e.target.closest('[data-auth="open"]');
    if (!el) return;
    e.preventDefault();
    const panel = el.dataset.panel || 'login';
    openModal(panel);
  });

  // Cerrar
  if (closeBtn) closeBtn.addEventListener('click', closeModal);
  overlay.addEventListener('click', (e) => { if (e.target === overlay) closeModal(); });
  document.addEventListener('keydown', (e) => { if (e.key === 'Escape') closeModal(); });

  // Toggle entre paneles dentro del modal
  if (toggleRegister) toggleRegister.addEventListener('click', (e) => { e.preventDefault(); show('register'); });
  if (toggleLogin) toggleLogin.addEventListener('click', (e) => { e.preventDefault(); show('login'); });

  // Submits fake: “logueamos” y cerramos
  if (loginForm) loginForm.addEventListener('submit', (e) => { e.preventDefault(); closeModal(); });
  // Submits: validar antes de cerrar
  if (registerForm) registerForm.addEventListener('submit', (e) => {
    e.preventDefault();

    // limpiamos espacios
    const first = registerForm.querySelector('#firstName');
    const last = registerForm.querySelector('#lastName');
    [first, last].forEach(i => i && (i.value = i.value.trim()));

    // validación nativa HTML5 de todos los campos requeridos
    if (!registerForm.checkValidity()) {
      registerForm.reportValidity();
      return;
    }

    // TODO: acá enviar los datos al backend
    closeModal();
  });

})();
