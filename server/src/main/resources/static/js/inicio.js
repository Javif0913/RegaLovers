// src/main/resources/static/js/app.js

document.addEventListener('DOMContentLoaded', () => {
    const loadUsersBtn = document.getElementById('loadUsersBtn');
    const loadQuestionsBtn = document.getElementById('loadQuestionsBtn');
    const loadEventsBtn = document.getElementById('loadEventsBtn');
    const dataDisplay = document.getElementById('dataDisplay');

    // Cargar datos de una API
    async function fetchData(endpoint) {
        dataDisplay.innerHTML = '<p>Cargando datos...</p>'; // Mensaje de carga
        try {

            const response = await fetch(`http://127.0.0.1:8080/api/${endpoint}`); // Realiza la solicitud a la API de Ktor (con el prefijo /api/)

            if (!response.ok) {

                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            dataDisplay.innerHTML = `
                <h2 class="font-semibold text-lg mb-2">Datos de /api/${endpoint}:</h2>
                <pre class="bg-white p-3 rounded-md overflow-auto">${JSON.stringify(data, null, 2)}</pre>
            `;
        } catch (error) {
            // Maneja cualquier error que ocurra durante la solicitud o el procesamiento
            dataDisplay.innerHTML = `<p class="text-red-500">Error al cargar datos: ${error.message}</p>`;
            console.error('Error fetching data:', error);
        }
    }

    // Eventos de clic a los botones
    loadUsersBtn.addEventListener('click', () => fetchData('usuarios'));
    loadQuestionsBtn.addEventListener('click', () => fetchData('questions'));
    loadEventsBtn.addEventListener('click', () => fetchData('eventos'));
});
