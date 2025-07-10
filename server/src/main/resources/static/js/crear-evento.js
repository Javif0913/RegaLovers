// src/main/resources/static/js/crear-evento.js

document.addEventListener('DOMContentLoaded', () => {
    const createEventForm = document.getElementById('createEventForm');
    const messageDisplay = document.getElementById('message');

    const usuario = JSON.parse(localStorage.getItem("usuario"));
    const editEventId = localStorage.getItem("editEventId");

    if (!usuario || !usuario.id) {
        messageDisplay.textContent = "Sesión no encontrada. Redirigiendo a login...";
        messageDisplay.className = "mt-4 text-center text-red-500";
        setTimeout(() => window.location.href = "/login.html", 2000);
        return;
    }

    // Si estamos en modo edición, cargar los datos del evento
    if (editEventId) {
        fetch(`/api/eventos/${editEventId}`)
            .then(res => res.ok ? res.json() : Promise.reject("No se pudo cargar el evento"))
            .then(evento => {
                createEventForm.querySelector('#nombre').value = evento.nombre || "";
                createEventForm.querySelector('#descripcion').value = evento.descripcion || "";
                createEventForm.querySelector('#lugar').value = evento.lugar || "";
                createEventForm.querySelector('#imagen').value = evento.imagen || "";
                createEventForm.querySelector('#tipo_evento').value = evento.tipo_evento || "";
                createEventForm.querySelector('#estado').value = evento.estado || "Pendiente";
            })
            .catch(error => {
                messageDisplay.textContent = `Error al cargar evento: ${error}`;
                messageDisplay.className = "mt-4 text-center text-red-500";
            });
    }

    createEventForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        messageDisplay.textContent = editEventId ? "Actualizando evento..." : "Creando evento...";
        messageDisplay.className = "mt-4 text-center text-gray-600";

        const formData = new FormData(createEventForm);
        const eventData = {
            id_organizador: usuario.id,
            nombre: formData.get('nombre'),
            descripcion: formData.get('descripcion'),
            lugar: formData.get('lugar'),
            imagen: formData.get('imagen'),
            tipo_evento: formData.get('tipo_evento'),
            estado: formData.get('estado')
        };

        const url = editEventId ? `/api/eventos/${editEventId}` : 'http://127.0.0.1:8080/api/eventos';
        const method = editEventId ? 'PUT' : 'POST';

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(eventData)
            });

            if (response.ok) {
                const result = await response.json();
                console.log("Evento creado:", result);

                if (editEventId) {
                    messageDisplay.textContent = `Evento actualizado correctamente. Redirigiendo...`;
                    localStorage.removeItem("editEventId");
                    setTimeout(() => window.location.href = "/mis-eventos", 1500);
                } else {
                    // Si se crea nuevo evento, guardar ID y redirigir para añadir regalos
                    localStorage.setItem('currentEventId', result.id);
                    messageDisplay.textContent = `Evento '${result.nombre}' creado exitosamente. Redirigiendo para añadir regalos...`;
                    setTimeout(() => window.location.href = "/anadir-regalos", 1500);
                }

                messageDisplay.className = "mt-4 text-center text-green-600";

            } else {
                const errorData = await response.json();
                messageDisplay.textContent = `Error al ${editEventId ? 'actualizar' : 'crear'} evento: ${errorData.message || response.statusText}`;
                messageDisplay.className = "mt-4 text-center text-red-500";
            }
        } catch (error) {
            messageDisplay.textContent = `Error de conexión: ${error.message}`;
            messageDisplay.className = "mt-4 text-center text-red-500";
            console.error(error);
        }
    });
});
