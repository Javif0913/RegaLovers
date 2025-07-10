package cl.usm.tel335.data

import cl.usm.tel335.database.Database
import cl.usm.tel335.database.Eventos
import cl.usm.tel335.database.Question
import cl.usm.tel335.model.QuestionDto
import cl.usm.tel335.database.Usuarios
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import cl.usm.tel335.database.Regalos
import cl.usm.tel335.database.Participantes
import cl.usm.tel335.model.UsuariosDto
import cl.usm.tel335.model.EventosDto
import cl.usm.tel335.model.ParticipantesDto
import cl.usm.tel335.model.RegalosDto
import cl.usm.tel335.model.LoginRequest
import kotlinx.serialization.SerialName
import kotlin.Long


import kotlinx.datetime.Clock // Para manejar fechas como timestamps


internal class AppDatabase(driverFactory: DriverFactory) {
    private val driver = driverFactory.createDriver() // Mantener el driver para la creación del esquema
    private val dataBase = Database(driver)
    internal val questionQueries = dataBase.questionQueries

    //PROYECTO SEGUN TABLAS
    internal val usuariosQueries = dataBase.usuariosQueries
    internal val eventosQueries = dataBase.eventosQueries
    internal val regalosQueries = dataBase.regalosQueries
    internal val participantesQueries = dataBase.participantesQueries
    //internal val commonQueries = dataBase.commonQueries


    // Funciones para Question
    internal fun getAllQuestions(): List<QuestionDto> {
        val questionsDto = questionQueries.selectAll().executeAsList().map { it.toDTO() }
        return questionsDto
    }

    // Funciones para Usuarios
    internal fun getAllUsuarios(): List<UsuariosDto> {
        return usuariosQueries.selectAllUsuarios().executeAsList().map { it.toDTO() }
    }

    internal fun insertUsuario(nickname: String, email: String, contrasena_hash: String, avatar_imagen: String?) {
        usuariosQueries.insertUsuario(
            nickname = nickname,
            email = email,
            contrasena_hash = contrasena_hash,
            fecha_registro = Clock.System.now().toEpochMilliseconds(), // Genera la fecha actual
            avatar_imagen = avatar_imagen
        )
    }

    internal fun getUsuarioById(id: Long): UsuariosDto? {
        return usuariosQueries.selectUsuarioById(id).executeAsOneOrNull()?.toDTO()
    }

    internal fun getUsuarioByNickname(nickname: String): UsuariosDto? {
        return usuariosQueries.selectUsuarioByNickname(nickname).executeAsOneOrNull()?.toDTO()
    }

    internal fun updateUsuarioContrasena(id: Long, newContrasenaHash: String) {
        usuariosQueries.updateContrasena(newContrasenaHash, id)
    }

    internal fun deleteUsuarioById(id: Long) {
        usuariosQueries.deleteUsuarioById(id)
    }

    internal fun deleteAllUsuarios() {
        usuariosQueries.deleteAllUsuarios()
    }

    internal fun getUsuarioByEmail(email: String): UsuariosDto? {
        return usuariosQueries.selectUsuarioByEmail(email).executeAsOneOrNull()?.toDTO()
    }


    // Funciones Login Usuarios
    internal fun loginUsuario(email: String, contrasena: String): UsuariosDto? {
        return usuariosQueries.selectUsuarioByEmail(email)
            .executeAsOneOrNull()
            ?.takeIf { it.contrasena_hash == contrasena }
            ?.toDTO()
    }

    // Funciones para Eventos
    internal fun getAllEventos(): List<EventosDto> {
        return eventosQueries.selectAllEventos().executeAsList().map { it.toDTO() }
    }

    internal fun insertEventoAndGetId(
        id_organizador: Long,
        nombre: String,
        descripcion: String?,
        lugar: String?,
        imagen: String?,
        tipo_evento: String?,
        estado: String?
    ): Long {
        // Insercion y entrega de lo insertado
        return dataBase.transactionWithResult {
            eventosQueries.insertEvento( // Query de inserción
                id_organizador = id_organizador,
                nombre = nombre,
                descripcion = descripcion,
                lugar = lugar,
                imagen = imagen,
                tipo_evento = tipo_evento,
                estado = estado
            )

            eventosQueries.lastInsertRowId().executeAsOne() // ID actual insetado
        }
    }

    internal fun getEventoById(id: Long): EventosDto? {
        return eventosQueries.selectEventoById(id).executeAsOneOrNull()?.toDTO()
    }

    internal fun getEventosByOrganizador(id_organizador: Long): List<EventosDto> {
        return eventosQueries.selectEventosByOrganizador(id_organizador).executeAsList().map { it.toDTO() }
    }

    fun getEventosByUsuario(idUsuario: Long): List<EventosDto> {
        return eventosQueries.getEventosByUsuario(idUsuario, idUsuario).executeAsList().map {
            EventosDto(
                id = it.id,
                id_organizador = it.id_organizador,
                nombre = it.nombre,
                descripcion = it.descripcion,
                lugar = it.lugar,
                imagen = it.imagen,
                tipo_evento = it.tipo_evento,
                estado = it.estado
            )
        }
    }




    internal fun updateEvento(
        id: Long,
        nombre: String,
        descripcion: String?,
        lugar: String?,
        imagen: String?,
        tipo_evento: String?,
        estado: String?
    ) {
        eventosQueries.updateEvento(nombre, descripcion, lugar, imagen, tipo_evento, estado, id)
    }

    internal fun deleteEventoById(id: Long) {
        eventosQueries.deleteEventoById(id)
    }

    internal fun deleteAllEventos() {
        eventosQueries.deleteAllEventos()
    }


    // Funciones para Regalos
    internal fun getAllRegalos(): List<RegalosDto> {
        return regalosQueries.selectAllRegalos().executeAsList().map { it.toDTO() }
    }

    internal fun insertRegalo(
        id_evento: Long,
        nombre_regalo: String,
        descripcion: String?,
        precio_estimado: Double?,
        url_tienda: String?,
        imagen: String?,
        estado: String?
    ): Long { // Devuelve un Long
        regalosQueries.insertRegalo(
            id_evento = id_evento,
            nombre_regalo = nombre_regalo,
            descripcion = descripcion,
            precio_estimado = precio_estimado,
            url_tienda = url_tienda,
            imagen = imagen,
            estado = estado
        )
        // Retorna el ID de la última fila insertada, que es el ID del nuevo regalo.
        return regalosQueries.lastInsertRowId().executeAsOne()
    }

    internal fun getRegaloById(id: Long): RegalosDto? {
        return regalosQueries.selectRegaloById(id).executeAsOneOrNull()?.toDTO()
    }

    internal fun getRegalosByEvento(id_evento: Long): List<RegalosDto> {
        return regalosQueries.selectRegalosByEvento(id_evento).executeAsList().map { it.toDTO() }
    }

    internal fun updateRegalo(
        id: Long,
        nombre_regalo: String,
        descripcion: String?,
        precio_estimado: Double?,
        url_tienda: String?,
        imagen: String?,
        estado: String?
    ) {
        regalosQueries.updateRegalo(nombre_regalo, descripcion, precio_estimado, url_tienda, imagen, estado, id)
    }

    internal fun deleteRegaloById(id: Long) {
        regalosQueries.deleteRegaloById(id)
    }

    internal fun deleteAllRegalos() {
        regalosQueries.deleteAllRegalos()
    }


    // Funciones para Participantes
    internal fun getAllParticipantes(): List<ParticipantesDto> {
        return participantesQueries.selectAllParticipantes().executeAsList().map { it.toDTO() }
    }

    internal fun insertParticipante(id_evento: Long, id_usuario: Long) {
        participantesQueries.insertParticipante(id_evento, id_usuario)
    }

    internal fun deleteAllParticipantes() {
        participantesQueries.deleteAllParticipantes()
    }

    internal fun getParticipanteByUsuarioYEvento(id_usuario: Long, id_evento: Long): ParticipantesDto? {
        return participantesQueries.selectParticipanteByUsuarioYEvento(id_usuario, id_evento)
            .executeAsOneOrNull()?.toDTO()
    }


    // Funciones de extensión para mapeo a DTOs
    private fun Question.toDTO() = QuestionDto(id = this.id, text = this.text, category = this.category)

    private fun Usuarios.toDTO() = UsuariosDto(
        id = this.id,
        nickname = this.nickname,
        email = this.email,
        contrasena_hash = this.contrasena_hash,
        fecha_registro = this.fecha_registro,
        avatar_imagen = this.avatar_imagen
    )

    private fun Eventos.toDTO() = EventosDto(
        id = this.id,
        id_organizador = this.id_organizador,
        nombre = this.nombre,
        descripcion = this.descripcion,
        lugar = this.lugar,
        imagen = this.imagen,
        tipo_evento = this.tipo_evento,
        estado = this.estado
    )

    private fun Regalos.toDTO() = RegalosDto(
        id = this.id,
        id_evento = this.id_evento,
        nombre_regalo = this.nombre_regalo,
        descripcion = this.descripcion,
        precio_estimado = this.precio_estimado,
        url_tienda = this.url_tienda,
        imagen = this.imagen,
        estado = this.estado
    )

    private fun Participantes.toDTO() = ParticipantesDto(
        id_evento = this.id_evento,
        id_usuario = this.id_usuario
    )
}