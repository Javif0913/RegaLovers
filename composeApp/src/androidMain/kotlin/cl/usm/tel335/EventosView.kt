package cl.usm.tel335
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import cl.usm.tel335.model.EventosDto
import androidx.compose.ui.Alignment
import cl.usm.tel335.data.AppDatabase
import cl.usm.tel335.data.DriverFactory
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.Scaffold
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Delete
import cl.usm.tel335.model.ParticipantesDto
import cl.usm.tel335.model.UsuariosDto

import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextAlign


import android.widget.Toast





@Composable
fun EventosView(
    eventos: List<EventosDto>, // Eventos creados por el usuario
    eventosInvitado: List<EventosDto>, // Eventos donde el usuario es participante
    onVerClick: (EventosDto, Boolean) -> Unit, // Añadimos parámetro para saber si es evento propio
    onEliminarClick: (Long) -> Unit,
    onAgregarEventoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- SECCIÓN "MIS EVENTOS" ---
        Text(
            text = "Mis Eventos",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (eventos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No has creado ningún evento",
                    color = Color.DarkGray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(eventos) { evento ->
                    EventoItem(
                        evento = evento,
                        onVerClick = { onVerClick(evento, true) },
                        onEliminarClick = { onEliminarClick(evento.id) },
                        esPropio = true
                    )
                }
            }
        }

        // --- SECCIÓN "EVENTOS INVITADO" ---
        Text(
            text = "Eventos Invitado",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp, top = 16.dp)
        )

        if (eventosInvitado.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No tienes eventos como invitado",
                    color = Color.DarkGray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(eventosInvitado) { evento ->
                    EventoItem(
                        evento = evento,
                        onVerClick = { onVerClick(evento, false) },
                        onEliminarClick = {}, // No se muestra el botón eliminar
                        esPropio = false
                    )
                }
            }
        }

        // --- BOTÓN "AGREGAR EVENTO" ---
        Button(
            onClick = onAgregarEventoClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
        ) {
            Text("Agregar Evento")
        }
    }
}



@Composable
fun EventoDetalleView(
    evento: EventosDto,
    esPropio: Boolean,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { AppDatabase(DriverFactory(context)) }
    val participantes = remember { mutableStateListOf<ParticipantesDto>() }
    val showDialog = remember { mutableStateOf(false) }
    var nicknameInput by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(evento.id) {
        withContext(Dispatchers.IO) {
            participantes.addAll(database.getParticipantesByEvento(evento.id))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Evento") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = evento.nombre, style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = evento.descripcion ?: "No disponible", modifier = Modifier.padding(top = 4.dp))
            Text(text = "Lugar: ${evento.lugar}", fontSize = 14.sp)
            Text(text = "Tipo: ${evento.tipo_evento}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de participantes
            Text(text = "Participantes:", style = MaterialTheme.typography.h6)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(participantes) { participante ->
                    ParticipanteItem(
                        participante = participante,
                        database = database,
                        onParticipanteEliminado = { participantes.remove(participante) },
                        mostrarEliminar = esPropio
                    )
                }
            }

            // Botón para agregar participante (solo visible si es evento propio)
            if (esPropio) {
                Button(
                    onClick = { showDialog.value = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Agregar Participante")
                }
            }
        }
    }

    // Diálogo para agregar participante (solo si es evento propio)
    if (esPropio && showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Agregar Participante") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nicknameInput,
                        onValueChange = { nicknameInput = it },
                        label = { Text("Nickname del usuario") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val usuario = withContext(Dispatchers.IO) {
                                database.getUsuarioByNickname(nicknameInput)
                            }?.takeIf { it.id != -1L }

                            if (usuario != null) {
                                // Verificar si el usuario ya está en el evento
                                val yaEsParticipante = withContext(Dispatchers.IO) {
                                    database.getParticipanteByIdEventoAndUsuario(evento.id, usuario.id) != null
                                }

                                if (yaEsParticipante) {
                                    Toast.makeText(context, "El usuario ya está en el evento", Toast.LENGTH_SHORT).show()
                                } else {
                                    withContext(Dispatchers.IO) {
                                        database.insertParticipante(evento.id, usuario.id)
                                    }
                                    ParticipantesDto(evento.id, usuario.id).apply {
                                        participantes.add(this)
                                    }
                                    showDialog.value = false
                                    nicknameInput = ""
                                }
                            } else {
                                Toast.makeText(context, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}





@Composable
fun ParticipanteItem(
    participante: ParticipantesDto,
    database: AppDatabase,
    onParticipanteEliminado: () -> Unit,
    mostrarEliminar: Boolean
) {
    val usuario = remember { mutableStateOf<UsuariosDto?>(null) }

    LaunchedEffect(participante.id_usuario) {
        withContext(Dispatchers.IO) {
            usuario.value = database.getUsuarioById(participante.id_usuario)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier.weight(1f),
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (usuario.value != null) {
                    Text(text = "Nombre: ${usuario.value!!.nickname}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Correo: ${usuario.value!!.email}")
                } else {
                    Text(text = "Cargando información del usuario...", fontStyle = FontStyle.Italic)
                }
            }
        }
        if (mostrarEliminar) {
            EliminarParticipanteButton(
                id_evento = participante.id_evento,
                id_usuario = participante.id_usuario,
                database = database,
                onParticipanteEliminado = onParticipanteEliminado
            )
        }
    }
}




@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
        Text(text = value)
    }
}

@Composable
fun EventoItem(
    evento: EventosDto,
    onVerClick: () -> Unit,
    onEliminarClick: () -> Unit,
    esPropio: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface // Mismo color para todos los eventos
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = evento.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                Row {
                    TextButton(
                        onClick = onVerClick,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colors.primary
                        )
                    ) {
                        Text("Ver")
                    }
                    if (esPropio) { // Solo mostrar el botón eliminar si es evento propio
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = onEliminarClick,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.Red
                            )
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = evento.descripcion ?: "Sin descripción", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Estado: ${evento.estado}", fontSize = 14.sp)
        }
    }
}


@Composable
fun EliminarParticipanteButton(
    id_evento: Long,
    id_usuario: Long,
    database: AppDatabase,
    onParticipanteEliminado: () -> Unit
) {
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    database.deleteParticipanteByIdEventoAndUsuario(id_evento, id_usuario)
                }
                onParticipanteEliminado()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Eliminar",
            tint = Color.Red
        )
    }
}






