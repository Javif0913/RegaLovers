package cl.usm.tel335.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import cl.usm.tel335.model.UsuariosDto
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Menu
import kotlinx.coroutines.launch
import cl.usm.tel335.model.EventosDto
import cl.usm.tel335.data.AppDatabase
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import cl.usm.tel335.data.DriverFactory
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.Scaffold
import androidx.compose.material.ButtonDefaults

import cl.usm.tel335.R


@Composable
fun UserScreen(
    usuario: UsuariosDto,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val database = remember { AppDatabase(DriverFactory(context)) }

    var showEventos by remember { mutableStateOf(true) }
    var selectedEvento by remember { mutableStateOf<EventosDto?>(null) }
    var showAgregarEventoDialog by remember { mutableStateOf(false) }
    var nuevoEventoNombre by remember { mutableStateOf("") }
    var nuevoDescripcion by remember { mutableStateOf("") }
    var nuevoLugar by remember { mutableStateOf("") }
    var nuevoTipoevento by remember { mutableStateOf("") }

    var eventos by remember { mutableStateOf<List<EventosDto>>(emptyList()) }
    var eventosInvitado by remember { mutableStateOf<List<EventosDto>>(emptyList()) }

    fun cargarEventos() {
        scope.launch {
            // Eventos creados por el usuario
            val misEventos = withContext(Dispatchers.IO) {
                database.getEventosByOrganizador(usuario.id)
            }
            eventos = misEventos

            // Eventos donde el usuario es participante
            val participantes = withContext(Dispatchers.IO) {
                database.selectEventoById(usuario.id)
            }
            val eventosInvitadoList = mutableListOf<EventosDto>()
            participantes.forEach { participante ->
                withContext(Dispatchers.IO) {
                    database.getEventoById(participante.id_evento)?.let {
                        eventosInvitadoList.add(it)
                    }
                }
            }
            eventosInvitado = eventosInvitadoList
        }
    }
    LaunchedEffect(Unit) {
        cargarEventos()
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.generico),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(usuario.nickname, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(usuario.email, fontSize = 14.sp, color = Color.Gray)
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                        },
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
                    ) {
                        Text("Editar perfil", color = Color.White)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        onLogout()
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = Color.White
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Cerrar sesión", color = Color.White)
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        if (showEventos) {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (showEventos) {
                    EventosView(
                        eventos = eventos,
                        eventosInvitado = eventosInvitado,
                        onVerClick = { evento, esPropio ->
                            selectedEvento = evento
                            showEventos = false
                        },
                        onEliminarClick = { idEvento ->
                            scope.launch(Dispatchers.IO) {
                                database.deleteParticipantesByEventoId(idEvento)
                                database.deleteEventoById(idEvento)
                                cargarEventos()
                            }
                        },
                        onAgregarEventoClick = { showAgregarEventoDialog = true }
                    )
                } else {
                    selectedEvento?.let { evento ->
                        EventoDetalleView(
                            evento = evento,
                            esPropio = eventos.any { it.id == evento.id },
                            onBackClick = {
                                selectedEvento = null
                                showEventos = true
                                cargarEventos()
                            }
                        )
                    }
                }
            }
        }
    }
    if (showAgregarEventoDialog) {
        AlertDialog(
            onDismissRequest = { showAgregarEventoDialog = false },
            title = { Text("Nuevo Evento") },
            text = {
                Column {
                    TextField(
                        value = nuevoEventoNombre,
                        onValueChange = { nuevoEventoNombre = it },
                        label = { Text("Nombre del evento") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = nuevoDescripcion,
                        onValueChange = { nuevoDescripcion = it },
                        label = { Text("Descripción del evento") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = nuevoLugar,
                        onValueChange = { nuevoLugar = it },
                        label = { Text("Lugar del evento") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    TextField(
                        value = nuevoTipoevento,
                        onValueChange = { nuevoTipoevento = it },
                        label = { Text("Tipo de evento") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch(Dispatchers.IO) {
                            database.insertEvento(
                                id_organizador = usuario.id,
                                nombre = nuevoEventoNombre,
                                descripcion = nuevoDescripcion,
                                lugar = nuevoLugar,
                                imagen = null,
                                tipo_evento = nuevoTipoevento,
                                estado = "Activo"
                            )
                            cargarEventos()
                            withContext(Dispatchers.Main) {
                                nuevoEventoNombre = ""
                                nuevoDescripcion = ""
                                nuevoLugar = ""
                                nuevoTipoevento = ""
                                showAgregarEventoDialog = false
                            }
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAgregarEventoDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}




