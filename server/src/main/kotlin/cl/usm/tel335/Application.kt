package cl.usm.tel335

import cl.usm.tel335.data.AppDatabase
import cl.usm.tel335.data.DriverFactory
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.request.receive
import java.io.File
import cl.usm.tel335.model.EventosDto
import cl.usm.tel335.model.LoginRequest
import cl.usm.tel335.model.RegalosDto
import cl.usm.tel335.model.ParticipantesDto

const val SERVER_PORT = 8080


fun main() {

    val driverFactory = DriverFactory()

    embeddedServer(Netty, port = SERVER_PORT, host = "127.0.0.1"){module(driverFactory)}
        .start(wait = true)
}

fun Application.module(driverFactory: DriverFactory) {

    install(io.ktor.server.plugins.cors.routing.CORS) {
        anyHost()
        allowHeader("Content-Type")
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
        allowMethod(io.ktor.http.HttpMethod.Get)
        allowMethod(io.ktor.http.HttpMethod.Options)
    }


    configureSerialization()

    val db = AppDatabase(driverFactory)

    routing {

        get("/") {

            val indexPath = this::class.java.classLoader.getResource("static/index.html")?.file
            if (indexPath != null) {
                call.respondFile(File(indexPath))
            } else {
                call.respond(HttpStatusCode.NotFound, "index.html no encontrado.")
            }
        }

        get("/invitado") {
            val page = this::class.java.classLoader.getResource("static/Invitado.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "firstWindow.html no encontrado.")
            }
        }


        get("/login") {
            val page = this::class.java.classLoader.getResource("static/login.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "login.html no encontrado.")
            }
        }

        get("/dashboard") {
            val page = this::class.java.classLoader.getResource("static/dashboard.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "dashboard.html no encontrado.")
            }
        }


        get("/crear-evento") {
            val page = this::class.java.classLoader.getResource("static/crear-evento.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "crear-evento.html no encontrado.")
            }
        }
        get("/mis-eventos") {
            val page = this::class.java.classLoader.getResource("static/mis-eventos.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "mis-eventos.html no encontrado.")
            }
        }
        get("/evento/{id}") {
            val id = call.parameters["id"]

            val page = this::class.java.classLoader.getResource("static/evento.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "evento.html no encontrado.")
            }
        }

        get("/anadir-regalos") {
            val page = this::class.java.classLoader.getResource("static/anadir-regalos.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "anadir-regalos.html no encontrado.")
            }
        }
        get("/ver-regalos") {
            val page = this::class.java.classLoader.getResource("static/ver-regalos.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "ver-regalos.html no encontrado.")
            }
        }

        get("/unirse-evento") {
            val page = this::class.java.classLoader.getResource("static/unirse-evento.html")?.file
            if (page != null) {
                call.respondFile(File(page))
            } else {
                call.respond(HttpStatusCode.NotFound, "unirse-evento.html no encontrado.")
            }
        }



        static("/assets") { //Lo utiliza la web para solicitar recursos .css y .js
            resources("static") // Esto mapea /assets a la carpeta src/main/resources/static
        }

        route("/api") {


            get("/usuarios") {
                val users = db.getAllUsuarios()
                call.respond(status = HttpStatusCode.OK, users)
            }
            get("/eventos") {
                val eventos = db.getAllEventos()
                call.respond(status = HttpStatusCode.OK, eventos)
            }
            get("/regalos") {
                val regalos = db.getAllRegalos()
                call.respond(status = HttpStatusCode.OK, regalos)
            }
            get("/participantes") {
                val participantes = db.getAllParticipantes()
                call.respond(status = HttpStatusCode.OK, participantes)
            }

            get("/eventos/usuario/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de usuario no válido.")
                    return@get
                }

                val eventos = db. getEventosByUsuario(id)
                call.respond(HttpStatusCode.OK, eventos)
            }

            post("/participantes/unirse") {
                try {
                    val data = call.receive<ParticipantesDto>()

                    val usuario = db.getUsuarioById(data.id_usuario)
                    val evento = db.getEventoById(data.id_evento)

                    if (usuario == null || evento == null) {
                        call.respond(HttpStatusCode.BadRequest, "Usuario o evento inválido.")
                        return@post
                    }

                    val yaParticipa = db.getParticipanteByUsuarioYEvento(
                        id_usuario = data.id_usuario,
                        id_evento = data.id_evento
                    )

                    if (yaParticipa != null) {
                        call.respond(HttpStatusCode.Conflict, "Ya estás unido a este evento.")
                        return@post
                    }

                    db.insertParticipante(data.id_evento, data.id_usuario)
                    call.respond(HttpStatusCode.Created, "Te has unido al evento correctamente.")

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error al unirse al evento.")
                }
            }


            post("/participantes/agregar") {
                try {
                    data class AgregarParticipanteRequest(val id_evento: Long, val email: String, val organizador_id: Long)

                    val data = call.receive<AgregarParticipanteRequest>()

                    val evento = db.getEventoById(data.id_evento)
                    if (evento == null) {
                        call.respond(HttpStatusCode.NotFound, "Evento no encontrado.")
                        return@post
                    }

                    if (evento.id_organizador != data.organizador_id) {
                        call.respond(HttpStatusCode.Forbidden, "Solo el organizador puede invitar a participantes.")
                        return@post
                    }

                    val usuarioInvitado = db.getUsuarioByEmail(data.email)
                    if (usuarioInvitado == null) {
                        call.respond(HttpStatusCode.NotFound, "No existe un usuario con ese email.")
                        return@post
                    }

                    val yaParticipa = db.getParticipanteByUsuarioYEvento(
                        id_usuario = usuarioInvitado.id,
                        id_evento = data.id_evento
                    )

                    if (yaParticipa != null) {
                        call.respond(HttpStatusCode.Conflict, "Ese usuario ya participa en el evento.")
                        return@post
                    }

                    db.insertParticipante(data.id_evento, usuarioInvitado.id)
                    call.respond(HttpStatusCode.Created, "Participante agregado correctamente.")

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error al agregar participante.")
                }
            }



            get("/regalos/evento/{id_evento}") {
                val idEvento = call.parameters["id_evento"]?.toLongOrNull()
                if (idEvento == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID de evento inválido.")
                    return@get
                }

                try {
                    val regalos = db.getRegalosByEvento(idEvento)
                    call.respond(HttpStatusCode.OK, regalos)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error al obtener regalos.")
                }
            }

            post("/login") {
                val loginRequest = call.receive<LoginRequest>()

                val usuario = db.loginUsuario(
                    email = loginRequest.email,
                    contrasena = loginRequest.contrasena
                )

                if (usuario != null) {
                    call.respond(HttpStatusCode.OK, usuario)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Correo o contraseña incorrectos.")
                }
            }

            post("/eventos") {
                try {

                    println("LLEGADA 1")

                    val eventRequest = call.receive<EventosDto>()

                    if (eventRequest.nombre.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("message" to "El nombre del evento es obligatorio."))
                        return@post
                    }

                    if (eventRequest.id_organizador == -1L) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("message" to "El ID del organizador es obligatorio y debe ser válido."))
                        return@post
                    }

                    println("LLEGO HASTA ACA")


                    val newEventId = db.insertEventoAndGetId(
                        id_organizador = eventRequest.id_organizador,
                        nombre = eventRequest.nombre,
                        descripcion = eventRequest.descripcion,
                        lugar = eventRequest.lugar,
                        imagen = eventRequest.imagen,
                        tipo_evento = eventRequest.tipo_evento,
                        estado = eventRequest.estado ?: "Pendiente" // Valor por defecto si no se proporciona
                    )

                    println("\n\n\n ${newEventId} \n\n\n")

                    val createdEventDto = eventRequest.copy(id = newEventId)
                    call.respond(HttpStatusCode.Created, createdEventDto)

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Error interno del servidor al crear evento: ${e.localizedMessage}"))
                    e.printStackTrace()
                }
            }

            post("/regalos") {
                try {
                    val giftRequest = call.receive<RegalosDto>()


                    if (giftRequest.nombre_regalo.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("message" to "El nombre del regalo es obligatorio."))
                        return@post
                    }
                    if (giftRequest.id_evento == -1L) { //-1L es el valor por defecto
                        call.respond(HttpStatusCode.BadRequest, mapOf("message" to "El ID del evento es obligatorio para asignar el regalo."))
                        return@post
                    }


                    val newGiftId = db.insertRegalo(
                        id_evento = giftRequest.id_evento,
                        nombre_regalo = giftRequest.nombre_regalo,
                        descripcion = giftRequest.descripcion,
                        precio_estimado = giftRequest.precio_estimado,
                        url_tienda = giftRequest.url_tienda,
                        imagen = giftRequest.imagen,
                        estado = giftRequest.estado ?: "Disponible"
                    )

                    // Si la insercion fue exitosa:
                    val createdGiftDto = giftRequest.copy(id = newGiftId)
                    call.respond(HttpStatusCode.Created, createdGiftDto)

                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Error interno del servidor al crear regalo: ${e.localizedMessage}"))
                    e.printStackTrace()
                }
            }
            put("/regalos/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                    return@put
                }

                val data = call.receive<RegalosDto>()
                db.updateRegalo(
                    id = id,
                    nombre_regalo = data.nombre_regalo,
                    descripcion = data.descripcion,
                    precio_estimado = data.precio_estimado,
                    url_tienda = data.url_tienda,
                    imagen = data.imagen,
                    estado = data.estado
                )
                call.respond(HttpStatusCode.OK, mapOf("message" to "Regalo actualizado."))
            }

            put("/eventos/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                    return@put
                }

                val updateData = call.receive<EventosDto>()
                db.updateEvento(
                    id = id,
                    nombre = updateData.nombre,
                    descripcion = updateData.descripcion,
                    lugar = updateData.lugar,
                    imagen = updateData.imagen,
                    tipo_evento = updateData.tipo_evento,
                    estado = updateData.estado
                )
                call.respond(HttpStatusCode.OK, mapOf("message" to "Evento actualizado correctamente"))
            }

            delete("/eventos/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@delete
                }

                try {
                    db.deleteEventoById(id)
                    call.respond(HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error al eliminar el evento")
                }
            }

            delete("/regalos/{id}") {
                val regaloId = call.parameters["id"]?.toLongOrNull()

                if (regaloId == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido.")
                    return@delete
                }

                val regalo = db.getRegaloById(regaloId)
                if (regalo == null) {
                    call.respond(HttpStatusCode.NotFound, "Regalo no encontrado.")
                    return@delete
                }

                try {
                    db.deleteRegaloById(regaloId)
                    call.respond(HttpStatusCode.OK, "Regalo eliminado con éxito.")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Error al eliminar el regalo.")
                }
            }


        }
    }
}

fun Application.configureSerialization(){
    install(ContentNegotiation){
        json()
    }
}