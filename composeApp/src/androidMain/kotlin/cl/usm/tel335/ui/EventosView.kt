package cl.usm.tel335.ui
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

import androidx.compose.ui.text.style.TextAlign

import cl.usm.tel335.model.RegalosDto
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import cl.usm.tel335.R


@Composable
fun EventosView(
    eventos: List<EventosDto>,
    eventosInvitado: List<EventosDto>,
    onVerClick: (EventosDto, Boolean) -> Unit,
    onEliminarClick: (Long) -> Unit,
    onAgregarEventoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
                        onEliminarClick = {},
                        esPropio = false
                    )
                }
            }
        }
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
    val regalos = remember { mutableStateListOf<RegalosDto>() }
    val showDialog = remember { mutableStateOf(false) }
    val showRegaloDialog = remember { mutableStateOf(false) }
    var nicknameInput by remember { mutableStateOf("") }
    var nombreRegaloInput by remember { mutableStateOf("") }
    var descripcionRegaloInput by remember { mutableStateOf("") }
    var precioEstimadoInput by remember { mutableStateOf("") }
    var urlTiendaInput by remember { mutableStateOf("") }
    var estadoRegaloInput by remember { mutableStateOf("disponible") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(evento.id) {
        withContext(Dispatchers.IO) {
            val participantesFromDB = database.getParticipantesByEvento(evento.id)
            val regalosFromDB = database.getRegalosByEvento(evento.id)

            participantes.clear()
            participantes.addAll(participantesFromDB)

            regalos.clear()
            regalos.addAll(regalosFromDB)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Evento") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                Column {
                    Text(text = evento.nombre, style = MaterialTheme.typography.h4)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = evento.descripcion ?: "No disponible", modifier = Modifier.padding(top = 4.dp))
                    Text(text = "Lugar: ${evento.lugar}", fontSize = 14.sp)
                    Text(text = "Tipo: ${evento.tipo_evento}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Participantes:", style = MaterialTheme.typography.h6)
                }
            }
            items(participantes) { participante ->
                ParticipanteItem(
                    participante = participante,
                    database = database,
                    onParticipanteEliminado = { participantes.remove(participante) },
                    mostrarEliminar = esPropio
                )
            }
            if (esPropio) {
                item {
                    Button(
                        onClick = { showDialog.value = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Agregar Participante")
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Lista de artículos:", style = MaterialTheme.typography.h6)
            }
            items(regalos) { regalo ->
                RegaloItem(
                    regalo = regalo,
                    database = database,
                    onRegaloEliminado = { regalos.remove(regalo) },
                    mostrarEliminar = esPropio
                )
            }
            if (esPropio) {
                item {
                    Button(
                        onClick = { showRegaloDialog.value = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text("Agregar artículo")
                    }
                }
            }
        }
    }

    // DIÁLOGOS (estos pueden estar fuera del Scaffold)

    // Diálogo para agregar participante
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
    if (esPropio && showRegaloDialog.value) {
        AlertDialog(
            onDismissRequest = { showRegaloDialog.value = false },
            title = { Text("Agregar artículo") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nombreRegaloInput,
                        onValueChange = { nombreRegaloInput = it },
                        label = { Text("Nombre del artículo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = descripcionRegaloInput,
                        onValueChange = { descripcionRegaloInput = it },
                        label = { Text("Descripción (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = precioEstimadoInput,
                        onValueChange = { precioEstimadoInput = it },
                        label = { Text("Precio estimado") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = urlTiendaInput,
                        onValueChange = { urlTiendaInput = it },
                        label = { Text("URL tienda (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val precio = precioEstimadoInput.toDoubleOrNull() ?: 0.0
                            withContext(Dispatchers.IO) {
                                database.insertRegalo(
                                    idEvento = evento.id,
                                    nombre = nombreRegaloInput,
                                    descripcion = descripcionRegaloInput.takeIf { it.isNotBlank() },
                                    precioEstimado = precio,
                                    urlTienda = urlTiendaInput.takeIf { it.isNotBlank() },
                                    estado = estadoRegaloInput
                                )
                            }
                            // Recargar la lista
                            val nuevosRegalos = withContext(Dispatchers.IO) {
                                database.getRegalosByEvento(evento.id)
                            }
                            regalos.clear()
                            regalos.addAll(nuevosRegalos)

                            showRegaloDialog.value = false
                            nombreRegaloInput = ""
                            descripcionRegaloInput = ""
                            precioEstimadoInput = ""
                            urlTiendaInput = ""
                        }
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRegaloDialog.value = false }) {
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.generico),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    if (usuario.value != null) {
                        Text(
                            text = "Nombre: ${usuario.value!!.nickname}",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Correo: ${usuario.value!!.email}")
                    } else {
                        Text(
                            text = "Cargando información del usuario...",
                            fontStyle = FontStyle.Italic
                        )
                    }
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

@Composable
fun RegaloItem(
    regalo: RegalosDto,
    database: AppDatabase,
    onRegaloEliminado: () -> Unit,
    mostrarEliminar: Boolean
) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.compra),
                    contentDescription = "Regalo",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Regalo: ${regalo.nombre_regalo}",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (!regalo.descripcion.isNullOrBlank()) {
                        Text(text = "Descripción: ${regalo.descripcion}")
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(text = "Precio: $${regalo.precio_estimado}")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Estado: ${regalo.estado}")
                }
            }
        }
        if (mostrarEliminar) {
            EliminarRegaloButton(
                idRegalo = regalo.id,
                database = database,
                onRegaloEliminado = onRegaloEliminado
            )
        }
    }
}


@Composable
fun EliminarRegaloButton(
    idRegalo: Long,
    database: AppDatabase,
    onRegaloEliminado: () -> Unit
) {
    val scope = rememberCoroutineScope()

    IconButton(
        onClick = {
            scope.launch {
                withContext(Dispatchers.IO) {
                    database.deleteRegaloById(idRegalo)
                }
                onRegaloEliminado()
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





