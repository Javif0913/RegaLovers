// src/main/resources/static/assets/js/mis-eventos.js

document.addEventListener("DOMContentLoaded", () => {
  cargarEventos();
});

let eventoEditandoId = null;

async function cargarEventos() {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  if (!usuario || !usuario.id) {
    alert("No se encontró el usuario. Por favor inicia sesión.");
    window.location.href = "/login";
    return;
  }

  try {
    const response = await fetch(`/api/eventos/usuario/${usuario.id}`);
    if (!response.ok) throw new Error("Error al cargar eventos.");

    const eventos = await response.json();
    const tabla = document.getElementById("tablaEventos");
    tabla.innerHTML = "";

    if (eventos.length === 0) {
      tabla.innerHTML = "<tr><td colspan='6' class='p-4 text-center text-gray-500'>No tienes eventos aún.</td></tr>";
      return;
    }

    eventos.forEach(evento => {
      const row = document.createElement("tr");

      if (eventoEditandoId === evento.id) {
        // Modo edición
        row.innerHTML = `
          <td class="py-2 px-4 border-b"><input id="editNombre" class="border p-1 w-full" value="${evento.nombre}"></td>
          <td class="py-2 px-4 border-b"><input id="editDescripcion" class="border p-1 w-full" value="${evento.descripcion || ""}"></td>
          <td class="py-2 px-4 border-b"><input id="editLugar" class="border p-1 w-full" value="${evento.lugar || ""}"></td>
          <td class="py-2 px-4 border-b"><input id="editTipo" class="border p-1 w-full" value="${evento.tipo_evento || ""}"></td>
          <td class="py-2 px-4 border-b">
            <select id="editEstado" class="border p-1 w-full">
              <option ${evento.estado === "Pendiente" ? "selected" : ""}>Pendiente</option>
              <option ${evento.estado === "Activo" ? "selected" : ""}>Activo</option>
              <option ${evento.estado === "Finalizado" ? "selected" : ""}>Finalizado</option>
            </select>
          </td>
          <td class="py-2 px-4 border-b">
            <button onclick="guardarEdicionEvento(${evento.id})" class="bg-green-600 hover:bg-green-700 text-white px-2 py-1 rounded text-sm">Guardar</button>
            <button onclick="cancelarEdicionEvento()" class="bg-gray-500 hover:bg-gray-600 text-white px-2 py-1 rounded text-sm ml-1">Cancelar</button>
          </td>
        `;
      } else {
        // Modo visualización
        row.innerHTML = `
          <td class="py-2 px-4 border-b">${evento.nombre}</td>
          <td class="py-2 px-4 border-b">${evento.descripcion || ""}</td>
          <td class="py-2 px-4 border-b">${evento.lugar || ""}</td>
          <td class="py-2 px-4 border-b">${evento.tipo_evento || ""}</td>
          <td class="py-2 px-4 border-b">${evento.estado || ""}</td>
          <td class="py-2 px-4 border-b space-x-1 space-y-1">
            <button onclick="editarEvento(${evento.id})" class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded text-sm">Editar</button>
            <button onclick="anadirRegalos(${evento.id})" class="bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded text-sm">Añadir Regalos</button>
            <button onclick="verRegalos(${evento.id})" class="bg-yellow-500 hover:bg-yellow-600 text-white px-3 py-1 rounded text-sm">Ver Regalos</button>
            <button onclick="eliminarEvento(${evento.id})" class="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded text-sm">Eliminar</button>
          </td>
        `;
      }

      tabla.appendChild(row);
    });
  } catch (error) {
    console.error(error);
    alert("Error al cargar los eventos del usuario.");
  }
}

function editarEvento(id) {
  if (eventoEditandoId !== null) {
    alert("Ya estás editando otro evento.");
    return;
  }
  eventoEditandoId = id;
  cargarEventos();
}

function cancelarEdicionEvento() {
  eventoEditandoId = null;
  cargarEventos();
}

async function guardarEdicionEvento(id) {
  const usuario = JSON.parse(localStorage.getItem("usuario"));
  if (!usuario || !usuario.id) return;

  const updatedEvento = {
    id_organizador: usuario.id,
    nombre: document.getElementById("editNombre").value,
    descripcion: document.getElementById("editDescripcion").value,
    lugar: document.getElementById("editLugar").value,
    imagen: "", // Opcional extender
    tipo_evento: document.getElementById("editTipo").value,
    estado: document.getElementById("editEstado").value
  };

  try {
    const response = await fetch(`/api/eventos/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(updatedEvento)
    });

    if (response.ok) {
      alert("Evento actualizado correctamente.");
      eventoEditandoId = null;
      cargarEventos();
    } else {
      const error = await response.json();
      alert("Error al actualizar: " + (error.message || response.statusText));
    }
  } catch (error) {
    console.error(error);
    alert("Error al conectar con el servidor.");
  }
}

function anadirRegalos(id) {
  localStorage.setItem("currentEventId", id);
  window.location.href = "/anadir-regalos";
}

function verRegalos(id) {
  localStorage.setItem("currentEventId", id);
  window.location.href = `/ver-regalos?eventoId=${id}`;
}

async function eliminarEvento(id) {
  if (!confirm("¿Estás seguro de que deseas eliminar este evento?")) return;

  try {
    const response = await fetch(`/api/eventos/${id}`, {
      method: "DELETE"
    });

    if (response.ok) {
      alert("Evento eliminado correctamente.");
      cargarEventos();
    } else {
      const error = await response.json();
      alert("Error al eliminar el evento: " + (error.message || response.statusText));
    }
  } catch (error) {
    console.error(error);
    alert("Error al conectar con el servidor.");
  }
}
