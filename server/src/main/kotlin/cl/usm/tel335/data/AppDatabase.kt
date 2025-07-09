package cl.usm.tel335.data

import cl.usm.tel335.database.Database // Esta es la clase de base de datos generada por SQLDelight
import cl.usm.tel335.database.Eventos
import cl.usm.tel335.database.Question
import cl.usm.tel335.model.QuestionDto
import cl.usm.tel335.database.Usuarios // <-- CORREGIDO: Importa la clase Usuario (singular)
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import cl.usm.tel335.database.Regalos
import cl.usm.tel335.database.Participantes
//Importar
import cl.usm.tel335.model.UsuariosDto
import cl.usm.tel335.model.EventosDto
import cl.usm.tel335.model.ParticipantesDto
import cl.usm.tel335.model.RegalosDto
import kotlinx.serialization.SerialName
import kotlin.Long

import kotlinx.datetime.Clock // Para manejar fechas como timestamps


public class AppDatabase(driverFactory: DriverFactory){
    private val driver = driverFactory.createDriver() // Mantener el driver para la creación del esquema
    private val dataBase = Database(driver)
    internal val questionQueries = dataBase.questionQueries

    //PROYECTO SEGUN TABLAS
    internal val usuariosQueries = dataBase.usuariosQueries
    internal val eventosQueries = dataBase.eventosQueries
    internal val regalosQueries = dataBase.regalosQueries
    internal val participantesQueries = dataBase.participantesQueries


    // --- Funciones para Question ---
    public fun getAllQuestions(): List<QuestionDto> {
        val questionsDto = questionQueries.selectAll().executeAsList().map { it.toDTO() }
        return questionsDto
    }

    // --- Funciones para Usuarios ---
    public fun getAllUsuarios(): List<UsuariosDto> {
        return usuariosQueries.selectAllUsuarios().executeAsList().map { it.toDTO() }
    }

    public fun insertUsuario(nickname: String, email: String, contrasena_hash: String, avatar_imagen: String?) {
        usuariosQueries.insertUsuario(
            nickname = nickname,
            email = email,
            contrasena_hash = contrasena_hash,
            fecha_registro = Clock.System.now().toEpochMilliseconds(), // Genera la fecha actual
            avatar_imagen = avatar_imagen
        )
    }

    public fun getUsuarioById(id: Long): UsuariosDto? {
        return usuariosQueries.selectUsuarioById(id).executeAsOneOrNull()?.toDTO()
    }

    public fun getUsuarioByNickname(nickname: String): UsuariosDto? {
        return usuariosQueries.selectUsuarioByNickname(nickname).executeAsOneOrNull()?.toDTO()
    }

    public fun updateUsuarioContrasena(id: Long, newContrasenaHash: String) {
        usuariosQueries.updateContrasena(newContrasenaHash, id)
    }

    public fun deleteUsuarioById(id: Long) {
        usuariosQueries.deleteUsuarioById(id)
    }

    public fun deleteAllUsuarios() {
        usuariosQueries.deleteAllUsuarios()
    }


    // --- Funciones para Eventos ---
    public fun getAllEventos(): List<EventosDto> {
        return eventosQueries.selectAllEventos().executeAsList().map { it.toDTO() }
    }

    public fun insertEvento(
        id_organizador: Long,
        nombre: String,
        descripcion: String?,
        lugar: String?,
        imagen: String?,
        tipo_evento: String?,
        estado: String?
    ) {
        eventosQueries.insertEvento(
            id_organizador = id_organizador,
            nombre = nombre,
            descripcion = descripcion,
            lugar = lugar,
            imagen = imagen,
            tipo_evento = tipo_evento,
            estado = estado
        )
    }

    public fun getEventoById(id: Long): EventosDto? {
        return eventosQueries.selectEventoById(id).executeAsOneOrNull()?.toDTO()
    }

    public fun getEventosByOrganizador(id_organizador: Long): List<EventosDto> {
        return eventosQueries.selectEventosByOrganizador(id_organizador).executeAsList().map { it.toDTO() }
    }

    public fun updateEvento(
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

    public fun deleteEventoById(id: Long) {
        eventosQueries.deleteEventoById(id)
    }

    public fun deleteAllEventos() {
        eventosQueries.deleteAllEventos()
    }


    // --- Funciones para Regalos ---
    public fun getAllRegalos(): List<RegalosDto> {
        return regalosQueries.selectAllRegalos().executeAsList().map { it.toDTO() }
    }

    public fun insertRegalo(
        id_evento: Long,
        nombre_regalo: String,
        descripcion: String?,
        precio_estimado: Double?,
        url_tienda: String?,
        imagen: String?,
        estado: String?
    ) {
        regalosQueries.insertRegalo(
            id_evento = id_evento,
            nombre_regalo = nombre_regalo,
            descripcion = descripcion,
            precio_estimado = precio_estimado,
            url_tienda = url_tienda,
            imagen = imagen,
            estado = estado
        )
    }

    public fun getRegaloById(id: Long): RegalosDto? {
        return regalosQueries.selectRegaloById(id).executeAsOneOrNull()?.toDTO()
    }

    public fun getRegalosByEvento(id_evento: Long): List<RegalosDto> {
        return regalosQueries.selectRegalosByEvento(id_evento).executeAsList().map { it.toDTO() }
    }

    public fun updateRegalo(
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

    public fun deleteRegaloById(id: Long) {
        regalosQueries.deleteRegaloById(id)
    }

    public fun deleteAllRegalos() {
        regalosQueries.deleteAllRegalos()
    }


    // --- Funciones para Participantes ---
    public fun getAllParticipantes(): List<ParticipantesDto> {
        return participantesQueries.selectAllParticipantes().executeAsList().map { it.toDTO() }
    }

    public fun insertParticipante(id_evento: Long, id_usuario: Long) {
        participantesQueries.insertParticipante(id_evento, id_usuario)
    }

    public fun getParticipanteById(id: Long): ParticipantesDto? {
        return participantesQueries.selectParticipanteById(id).executeAsOneOrNull()?.toDTO()
    }

    public fun getParticipantesByEvento(id_evento: Long): List<ParticipantesDto> {
        return participantesQueries.selectParticipantesByEvento(id_evento).executeAsList().map { it.toDTO() }
    }

    public fun getParticipantesByUsuario(id_usuario: Long): List<ParticipantesDto> {
        return participantesQueries.selectParticipantesByUsuario(id_usuario).executeAsList().map { it.toDTO() }
    }

    public fun deleteParticipanteById(id: Long) {
        participantesQueries.deleteParticipanteById(id)
    }

    public fun deleteAllParticipantes() {
        participantesQueries.deleteAllParticipantes()
    }


    // --- Funciones de extensión para mapeo a DTOs ---
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
        id = this.id,
        id_evento = this.id_evento,
        id_usuario = this.id_usuario
    )
}