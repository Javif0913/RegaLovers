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

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "127.0.0.1", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    configureSerialization()

    val db = AppDatabase(DriverFactory())

    routing {
        get("/") {
            call.respondText("Nueva base del proyecto :) !!")
        }

        get("/questions") {
            val questions = db.getAllQuestions()
            call.respond(status = HttpStatusCode.OK, questions)
        }

        get("/usuarios"){
            val users = db.getAllUsuarios()
            call.respond(status = HttpStatusCode.OK, users)
        }
        get("/eventos"){
            val eventos = db.getAllEventos()
            call.respond(status = HttpStatusCode.OK, eventos)
        }
        get("/regalos"){
            val regalos = db.getAllRegalos()
            call.respond(status = HttpStatusCode.OK, regalos)
        }
        get("/participantes"){
            val participantes = db.getAllParticipantes()
            call.respond(status = HttpStatusCode.OK, participantes)
        }

        get("/participantes/{id}"){

            val id = call.parameters["id"]?.toLongOrNull()

            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID de participante no válido.")
                return@get
            }
            val participante = db.getParticipanteById(id)
            if (participante != null) {
                call.respond(status = HttpStatusCode.OK, participante)
            } else {
                call.respond(HttpStatusCode.NotFound, "Participante con ID $id no encontrado.")
            }
        }

    }
}

fun Application.configureSerialization(){
    install(ContentNegotiation){
        json()
    }
}