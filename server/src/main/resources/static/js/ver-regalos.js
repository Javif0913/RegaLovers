document.addEventListener("DOMContentLoaded", async () => {
  const params = new URLSearchParams(window.location.search);
  const eventoId = params.get("eventoId");

  if (!eventoId) {
    alert("ID del evento no especificado.");
    return;
  }

  try {
    const response = await fetch(`/api/regalos/evento/${eventoId}`);
    const regalos = await response.json();
    const tbody = document.getElementById("regalosBody");

    regalos.forEach(regalo => {
      const row = document.createElement("tr");
      row.id = `row-${regalo.id}`;
      renderRow(row, regalo, false);
      tbody.appendChild(row);
    });

  } catch (error) {
    console.error("Error al cargar los regalos:", error);
    alert("Error al cargar los regalos.");
  }
});

function renderRow(row, regalo, editable) {
  row.innerHTML = "";

  if (!editable) {
    row.innerHTML = `
      <td class="py-2 px-4 border-b">${regalo.nombre_regalo}</td>
      <td class="py-2 px-4 border-b">${regalo.descripcion || ""}</td>
      <td class="py-2 px-4 border-b">${regalo.precio_estimado != null ? "$" + regalo.precio_estimado.toFixed(2) : ""}</td>
      <td class="py-2 px-4 border-b">${regalo.estado || ""}</td>
      <td class="py-2 px-4 border-b space-x-2">
        <button onclick="editarRegalo(${regalo.id})" class="bg-blue-500 hover:bg-blue-600 text-white font-semibold py-1 px-3 rounded-lg text-sm">Editar</button>
        <button onclick="eliminarRegalo(${regalo.id})" class="bg-red-500 hover:bg-red-600 text-white font-semibold py-1 px-3 rounded-lg text-sm">Eliminar</button>
      </td>
    `;
  } else {
    const nombreInput = createInput(regalo.nombre_regalo);
    const descripcionInput = createInput(regalo.descripcion || "");
    const precioInput = createInput(regalo.precio_estimado || "", "number");

    const estadoSelect = document.createElement("select");
    ["Disponible", "Reservado", "Entregado"].forEach(estado => {
      const option = document.createElement("option");
      option.value = estado;
      option.textContent = estado;
      if (regalo.estado === estado) option.selected = true;
      estadoSelect.appendChild(option);
    });
    estadoSelect.className = "border rounded px-2 py-1 w-full text-sm";

    const guardarBtn = document.createElement("button");
    guardarBtn.textContent = "Guardar";
    guardarBtn.className = "bg-green-500 hover:bg-green-600 text-white font-semibold py-1 px-3 rounded-lg text-sm mr-2";
    guardarBtn.onclick = async () => {
      const actualizado = {
        id: regalo.id,
        id_evento: regalo.id_evento,
        nombre_regalo: nombreInput.value,
        descripcion: descripcionInput.value,
        precio_estimado: parseFloat(precioInput.value),
        url_tienda: regalo.url_tienda || "",
        imagen: regalo.imagen || "",
        estado: estadoSelect.value
      };

      try {
        const res = await fetch(`/api/regalos/${regalo.id}`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(actualizado)
        });

        if (res.ok) {
          alert("Regalo actualizado correctamente.");
          renderRow(row, actualizado, false);
        } else {
          alert("Error al actualizar regalo.");
        }
      } catch (err) {
        console.error(err);
        alert("Error al conectar con el servidor.");
      }
    };

    const cancelarBtn = document.createElement("button");
    cancelarBtn.textContent = "Cancelar";
    cancelarBtn.className = "bg-gray-400 hover:bg-gray-500 text-white font-semibold py-1 px-3 rounded-lg text-sm";
    cancelarBtn.onclick = () => renderRow(row, regalo, false);

    const tdNombre = document.createElement("td");
    tdNombre.className = "py-2 px-4 border-b";
    tdNombre.appendChild(nombreInput);

    const tdDesc = document.createElement("td");
    tdDesc.className = "py-2 px-4 border-b";
    tdDesc.appendChild(descripcionInput);

    const tdPrecio = document.createElement("td");
    tdPrecio.className = "py-2 px-4 border-b";
    tdPrecio.appendChild(precioInput);

    const tdEstado = document.createElement("td");
    tdEstado.className = "py-2 px-4 border-b";
    tdEstado.appendChild(estadoSelect);

    const tdAcciones = document.createElement("td");
    tdAcciones.className = "py-2 px-4 border-b space-x-2";
    tdAcciones.appendChild(guardarBtn);
    tdAcciones.appendChild(cancelarBtn);

    row.appendChild(tdNombre);
    row.appendChild(tdDesc);
    row.appendChild(tdPrecio);
    row.appendChild(tdEstado);
    row.appendChild(tdAcciones);
  }
}

function createInput(value, type = "text") {
  const input = document.createElement("input");
  input.type = type;
  input.value = value;
  input.className = "border rounded px-2 py-1 w-full text-sm";
  return input;
}

function editarRegalo(id) {
  const row = document.getElementById(`row-${id}`);
  const regalo = {
    id,
    id_evento: parseInt(new URLSearchParams(window.location.search).get("eventoId")),
    nombre_regalo: row.children[0].textContent,
    descripcion: row.children[1].textContent,
    precio_estimado: parseFloat(row.children[2].textContent.replace("$", "")) || 0,
    estado: row.children[3].textContent,
    url_tienda: "", // para evitar error al hacer PUT
    imagen: ""
  };
  renderRow(row, regalo, true);
}

async function eliminarRegalo(id) {
  if (!confirm("¿Estás seguro de que deseas eliminar este regalo?")) return;

  try {
    const res = await fetch(`/api/regalos/${id}`, { method: "DELETE" });
    if (res.ok) {
      document.getElementById(`row-${id}`).remove();
      alert("Regalo eliminado correctamente.");
    } else {
      alert("Error al eliminar el regalo.");
    }
  } catch (err) {
    console.error(err);
    alert("Error al conectar con el servidor.");
  }
}
