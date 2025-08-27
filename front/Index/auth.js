// Modal login/registro muy simple (sin backend)
(function(){
  const overlay       = document.getElementById('authOverlay');
  const btn           = document.getElementById('accountBtn');
  const closeBtn      = document.getElementById('authClose');
  const loginPanel    = document.getElementById('loginPanel');
  const registerPanel = document.getElementById('registerPanel');
  const toggleRegister= document.getElementById('toggleRegister');
  const toggleLogin   = document.getElementById('toggleLogin');
  const loginForm     = document.getElementById('loginForm');
  const registerForm  = document.getElementById('registerForm');

  function openModal(panel="login"){
    overlay.hidden = false;
    document.body.classList.add('modal-open');
    show(panel);
  }
  function closeModal(){
    overlay.hidden = true;
    document.body.classList.remove('modal-open');
  }
  function show(panel){
    const isReg = panel === "register";
    loginPanel.hidden    = isReg;
    registerPanel.hidden = !isReg;
  }
  function setLoggedUI(name){
    btn.dataset.state = 'logged';
    btn.textContent = name || 'Abby';
    btn.classList.add('logged');
  }
  function setGuestUI(){
    btn.dataset.state = 'guest';
    btn.textContent = 'Registrarse';
    btn.classList.remove('logged');
  }

  // Estado inicial: invitado
  setGuestUI();

  // Abrir desde botón
  btn.addEventListener('click', () => {
    if (btn.dataset.state === 'logged') {
      // acá podrías abrir un menú de perfil
      return;
    }
    openModal('login');
  });

  // Cerrar
  closeBtn.addEventListener('click', closeModal);
  overlay.addEventListener('click', (e)=>{ if(e.target === overlay) closeModal(); });
  document.addEventListener('keydown', (e)=>{ if(e.key === 'Escape') closeModal(); });

  // Toggle login/registro
  if (toggleRegister) toggleRegister.addEventListener('click', (e)=>{ e.preventDefault(); show('register'); });
  if (toggleLogin)    toggleLogin.addEventListener('click', (e)=>{ e.preventDefault(); show('login'); });

  // Submit forms (fake)
  if (loginForm) loginForm.addEventListener('submit', (e)=>{
    e.preventDefault();
    setLoggedUI('Abby');
    closeModal();
  });
  if (registerForm) registerForm.addEventListener('submit', (e)=>{
    e.preventDefault();
    setLoggedUI('Abby');
    closeModal();
  });
})();
