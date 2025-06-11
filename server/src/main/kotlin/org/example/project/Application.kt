package org.example.project

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.example.database.AppDatabase





lateinit var database: AppDatabase



fun main() {
    initDatabase()
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    routing {
        get("/") {
            call.respondText("Tamos Activos")
        }
        get("/eventos/{id}") {
            val idParam = call.parameters["id"]
            val id = idParam?.toLongOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
                return@get
            }

            val eventoDB = database.eventosQueries.selectById(id).executeAsOneOrNull()

            if (eventoDB == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Evento no encontrado"))
            } else {
                val evento = Evento(
                    id = eventoDB.id,
                    nombre = eventoDB.nombre,
                    owner = eventoDB.owner,
                    fecha = eventoDB.fecha
                )
                call.respond(evento)
            }
        }


        get("/eventos") {
            val eventos = database.eventosQueries.selectAll().executeAsList().map {
                Evento(
                    id = it.id,
                    nombre = it.nombre,
                    owner = it.owner,
                    fecha = it.fecha
                )
            }
            call.respond(eventos)
        }
        post("/eventos") {
            val nuevoEvento = call.receive<CrearEventoRequest>()
            database.eventosQueries.insertEvento(
                nombre = nuevoEvento.nombre,
                owner = nuevoEvento.owner,
                fecha = nuevoEvento.fecha
            )
            call.respond(mapOf("mensaje" to "Evento creado correctamente"))
        }

        delete("/eventos/{id}") {
            val idparametro = call.parameters["id"]
            val id = idparametro?.toLongOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "El ID entregado no ha sido encontrado. Pruebe con otro ID"))
                return@delete
            }

            val evento = database.eventosQueries.selectById(id).executeAsOneOrNull()
            if (evento == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "El ID del evento entregado no ha sido encontrado. Pruebe con otro ID"))
            } else {
                database.eventosQueries.deleteById(id)
                call.respond(mapOf("mensaje" to "El evento seleccionado ha sido borrado"))
            }
        }

        put("/eventos/{id}") {
            val idparametro = call.parameters["id"]
            val id = idparametro?.toLongOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido, por favor ingrese un id valido"))
                return@put
            }

            val evento = database.eventosQueries.selectById(id).executeAsOneOrNull()
            if (evento == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "El evento no fue encontrado"))
                return@put
            }

            val request = call.receive<UpdateEventoRequest>()
            database.eventosQueries.updateEvento(
                nombre = request.nombre,
                owner = request.owner,
                fecha = request.fecha,
                id = id
            )

            call.respond(mapOf("mensaje" to "Evento actualizado correctamente"))
        }
        get("/eventos/{id}/regalos") {
            val eventoId = call.parameters["id"]?.toLongOrNull()
            if (eventoId == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
                return@get
            }

            val regalos = database.regalosQueries.selectRegalosPorEvento(eventoId).executeAsList().map {
                Regalo(
                    id = it.id,
                    evento_id = it.evento_id,
                    descripcion = it.descripcion,
                    claimed = it.claimed != 0L // 👈 conversión manual a Boolean
                )
            }

            call.respond(regalos)
        }


        post("/eventos/{id}/regalos") {
            val eventoId = call.parameters["id"]?.toLongOrNull()
            if (eventoId == null) return@post call.respond(HttpStatusCode.BadRequest)

            val nuevoRegalo = call.receive<CrearRegaloRequest>()
            database.regalosQueries.insertRegalo(eventoId, nuevoRegalo.descripcion)
            call.respond(HttpStatusCode.Created, mapOf("mensaje" to "Regalo agregado"))
        }

        put("/regalos/{id}/claim") {
            val regaloId = call.parameters["id"]?.toLongOrNull()
            if (regaloId == null) return@put call.respond(HttpStatusCode.BadRequest)

            database.regalosQueries.updateClaim(regaloId)
            call.respond(mapOf("mensaje" to "Regalo marcado como reclamado"))
        }
    }
}

@Serializable
data class Evento(
    val id: Long,
    val nombre: String,
    val owner: String,
    var fecha: String
)

@Serializable
data class CrearEventoRequest(
    val nombre: String,
    val owner: String,
    val fecha: String
)

@Serializable
data class UpdateEventoRequest(
    val nombre: String,
    val owner: String,
    val fecha: String
)

@Serializable
data class Regalo(
    val id: Long,
    val evento_id: Long,
    val descripcion: String,
    val claimed: Boolean
)

@Serializable
data class CrearRegaloRequest(val descripcion: String)

fun initDatabase() {
    val driver = JdbcSqliteDriver("jdbc:sqlite:eventos.db")
    val dbFile = java.io.File("eventos.db")

    if (!dbFile.exists()) {
        AppDatabase.Schema.create(driver)
    }

    database = AppDatabase(driver) // ← SIN adaptadores

    if (database.eventosQueries.selectAll().executeAsList().isEmpty()) {
        database.eventosQueries.insertEvento("Cumpleaños Juan", "Juan", "2025-06-05")
        database.eventosQueries.insertEvento("Aniversario Ana y Luis", "Luis", "2025-07-20")
    }
}



