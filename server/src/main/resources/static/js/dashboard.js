const usuario = JSON.parse(localStorage.getItem("usuario"));

if (!usuario) {
  alert("Sesión no encontrada. Redirigiendo a login.");
  window.location.href = "/login";
} else {
  document.getElementById("userInfo").innerText = `Hola, ${usuario.nickname}`;
}

if (usuario){
  document.getElementById("correoMenu").innerText = `Email:
  ${usuario.email}`;
}

document.getElementById("misEventos").innerText = `Mis Eventos`;
document.getElementById("otrosEventos").innerText = `Otros Eventos`;

if (usuario){
  document.getElementById("nombreMenu").innerText = `Usuario:
  ${usuario.nickname}`;
}

function cerrarSesion() {
  localStorage.clear();
  window.location.href = "/";
}

// Menu lateral
function openNav() {
    document.getElementById("mySidebar").style.width = "250px"; // Ancho para abrir
    document.getElementById("mySidebar").classList.add("open");
}

function closeNav() {
    document.getElementById("mySidebar").style.width = "0"; // Ancho a 0 para cerrar
    document.getElementById("mySidebar").classList.remove("open"); // Remueve la clase 'open'
}

// Funciones globales para que puedan ser llamadas desde onclick en HTML
window.openNav = openNav;
window.closeNav = closeNav;