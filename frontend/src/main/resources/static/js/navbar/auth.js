// /static/js/auth.js
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

  // recordar el último trigger para devolver el foco al cerrar
  let lastTrigger = null;

  // Helpers
  function show(panel) {
    const isReg = panel === 'register';
    if (loginPanel) loginPanel.hidden = isReg;
    if (registerPanel) registerPanel.hidden = !isReg;

    // foco inicial en el primer input del panel visible
    const container = isReg ? registerPanel : loginPanel;
    const first = container && container.querySelector('input, select, textarea, button:not([type=button])');
    if (first) first.focus();
  }

  function openModal(panel = 'login', trigger = null) {
    lastTrigger = trigger || document.activeElement;
    overlay.hidden = false;
    document.body.classList.add('modal-open');
    overlay.setAttribute('aria-hidden', 'false');
    show(panel);
  }

  function closeModal() {
    overlay.hidden = true;
    document.body.classList.remove('modal-open');
    overlay.setAttribute('aria-hidden', 'true');
    // devolver foco al trigger
    if (lastTrigger && typeof lastTrigger.focus === 'function') {
      lastTrigger.focus();
    }
  }

  // Exponer para uso opcional desde otros scripts
  window.openAuthModal = openModal;

  // Delegación: abrir modal con data-auth="open"
  document.addEventListener('click', (e) => {
    const el = e.target.closest('[data-auth="open"]');
    if (!el) return;
    e.preventDefault();
    const panel = el.dataset.panel || 'login';
    openModal(panel, el); // NEW: pasar trigger
  });

  // Cerrar
  if (closeBtn) closeBtn.addEventListener('click', closeModal);
  overlay.addEventListener('click', (e) => { if (e.target === overlay) closeModal(); });
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape' && !overlay.hidden) closeModal();   // pequeño guard
  });

  // Toggle entre paneles dentro del modal
  if (toggleRegister) toggleRegister.addEventListener('click', (e) => { e.preventDefault(); show('register'); });
  if (toggleLogin)    toggleLogin.addEventListener('click',    (e) => { e.preventDefault(); show('login'); });

  // Submits fake login
  if (loginForm) loginForm.addEventListener('submit', (e) => {
    e.preventDefault();
    // validaciones nativas
    if (!loginForm.checkValidity && !loginForm.checkValidity?.()) {
      loginForm.reportValidity?.();
      return;
    }
    // TODO: hook a backend si lo necesitás
    closeModal();
  });

  // Submit registro: validación HTML5
  if (registerForm) registerForm.addEventListener('submit', (e) => {
    e.preventDefault();

    // limpiar espacios
    const first = registerForm.querySelector('#firstName');
    const last  = registerForm.querySelector('#lastName');
    [first, last].forEach(i => i && (i.value = i.value.trim()));

    if (!registerForm.checkValidity()) {
      registerForm.reportValidity();
      return;
    }

    // TODO: enviar al backend
    closeModal();
  });

  // Toggle password visibility - manejo de clicks en los botones de ojo
  overlay.addEventListener('click', (e) => {
    const toggleBtn = e.target.closest('.toggle-password');
    if (!toggleBtn) return;

    e.preventDefault();
    e.stopPropagation();

    const targetId = toggleBtn.getAttribute('data-target');
    const passwordInput = document.getElementById(targetId);
    if (!passwordInput) return;

    const eyeIcon = toggleBtn.querySelector('.eye-icon');
    const eyeOffIcon = toggleBtn.querySelector('.eye-off-icon');

    if (passwordInput.type === 'password') {
      // Mostrar contraseña
      passwordInput.type = 'text';
      if (eyeIcon) eyeIcon.style.display = 'none';
      if (eyeOffIcon) eyeOffIcon.style.display = 'block';
      toggleBtn.setAttribute('aria-label', 'Ocultar contraseña');
    } else {
      // Ocultar contraseña
      passwordInput.type = 'password';
      if (eyeIcon) eyeIcon.style.display = 'block';
      if (eyeOffIcon) eyeOffIcon.style.display = 'none';
      toggleBtn.setAttribute('aria-label', 'Mostrar contraseña');
    }
  });

})();
