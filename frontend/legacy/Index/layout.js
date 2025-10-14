// Mide alturas reales de header y subnav y las deja en variables CSS.
// Así la .layout calcula su alto y el feed scrollea solo.
(function () {
  const root   = document.documentElement;
  const header = document.getElementById('navbar');
  const subnav = document.getElementById('subnav');

  function setHeights(){
    if (header) root.style.setProperty('--header-h', Math.round(header.getBoundingClientRect().height) + 'px');
    if (subnav) root.style.setProperty('--subnav-h', Math.round(subnav.getBoundingClientRect().height) + 'px');
  }

  window.addEventListener('load', setHeights);
  window.addEventListener('resize', setHeights);
})();
