// src/main/resources/static/js/anadir-regalos.js

document.addEventListener('DOMContentLoaded', () => {
    const addGiftForm = document.getElementById('addGiftForm');
    const messageDisplay = document.getElementById('message');
    const eventNameDisplay = document.getElementById('eventName');
    const finishButton = document.getElementById('finishAddingGifts');

    // Obtener evento guardado en local
    const currentEventId = localStorage.getItem('currentEventId');

    if (!currentEventId) {
        messageDisplay.textContent = "No se encontró un ID de evento. Redirigiendo al dashboard.";
        messageDisplay.className = "mt-4 text-center text-red-500";
        setTimeout(() => {
            window.location.href = "/dashboard";
        }, 2000);
        return; // Detener la ejecución del script
    }

    eventNameDisplay.textContent = `Añadiendo regalos para el Evento ID: ${currentEventId}`; //cambiar


    // 2. Manejar el envío del formulario de regalo
    addGiftForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        messageDisplay.textContent = "Guardando regalo...";
        messageDisplay.className = "mt-4 text-center text-gray-600";

        const formData = new FormData(addGiftForm);
        const giftData = {
            id_evento: parseInt(currentEventId),
            nombre_regalo: formData.get('nombre_regalo'),
            descripcion: formData.get('descripcion'),
            precio_estimado: parseFloat(formData.get('precio_estimado')) || null,
            url_tienda: formData.get('url_tienda'),
            imagen: formData.get('imagen'),
            estado: formData.get('estado')
        };

        try {
            const response = await fetch('/api/regalos', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(giftData)
            });

            if (response.ok) {
                const newGift = await response.json();
                messageDisplay.textContent = `Regalo '${newGift.nombre_regalo}' guardado exitosamente! Añade otro o finaliza.`;
                messageDisplay.className = "mt-4 text-center text-green-600";
                addGiftForm.reset();
            } else {
                const errorData = await response.json();
                messageDisplay.textContent = `Error al guardar regalo: ${errorData.message || response.statusText}`;
                messageDisplay.className = "mt-4 text-center text-red-500";
            }
        } catch (error) {
            messageDisplay.textContent = `Error de conexión: ${error.message}`;
            messageDisplay.className = "mt-4 text-center text-red-500";
            console.error('Error al enviar el formulario de regalo:', error);
        }
    });

        // Boton Finalizar
        finishButton.addEventListener('click', () => {
            localStorage.removeItem('currentEventId');
            window.location.href = "/dashboard";
    });
});
