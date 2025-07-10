package cl.usm.tel335.data

import cl.usm.tel335.database.Database
import cl.usm.tel335.database.Eventos
import cl.usm.tel335.database.Usuarios
import cl.usm.tel335.database.Regalos
import cl.usm.tel335.database.Participantes
import cl.usm.tel335.model.UsuariosDto
import cl.usm.tel335.model.EventosDto
import cl.usm.tel335.model.ParticipantesDto
import cl.usm.tel335.model.RegalosDto
import kotlin.Long
import kotlinx.datetime.Clock


public class AppDatabase(driverFactory: DriverFactory){
    private val driver = driverFactory.createDriver()
    private val dataBase = Database(driver)

    //PROYECTO SEGUN TABLAS
    internal val usuariosQueries = dataBase.usuariosQueries
    internal val eventosQueries = dataBase.eventosQueries
    internal val regalosQueries = dataBase.regalosQueries
    internal val participantesQueries = dataBase.participantesQueries


    // --- Funciones para Usuarios ---
    public fun getAllUsuarios(): List<UsuariosDto> {
        return usuariosQueries.selectAllUsuarios().executeAsList().map { it.toDTO() }
    }

    public fun insertUsuario(nickname: String, email: String, contrasena_hash: String, avatar_imagen: String?) {
        usuariosQueries.insertUsuario(
            nickname = nickname,
            email = email,
            contrasena_hash = contrasena_hash,
            fecha_registro = Clock.System.now().toEpochMilliseconds(),
            avatar_imagen = avatar_imagen
        )
    }

    public fun getUsuarioByEmail(email: String): UsuariosDto? {
        return usuariosQueries.selectUsuarioByEmail(email).executeAsOneOrNull()?.toDTO()
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

    fun insertRegalo(
        idEvento: Long,
        nombre: String,
        descripcion: String? = null, // Parámetro opcional
        precioEstimado: Double,
        urlTienda: String? = null,
        imagen: String? = null,
        estado: String = "disponible"
    )
{
        regalosQueries.insertRegalo(
            id_evento = idEvento,
            nombre_regalo = nombre,
            descripcion = descripcion,
            precio_estimado = precioEstimado,
            url_tienda = urlTienda,
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


    public fun getParticipantesByEvento(id_evento: Long): List<ParticipantesDto> {
        return participantesQueries.selectParticipantesByEvento(id_evento).executeAsList().map { it.toDTO() }
    }

    public fun deleteParticipanteByIdEventoAndUsuario(id_evento: Long, id_usuario: Long) {
        participantesQueries.deleteParticipanteByIdEventoAndUsuario(id_evento, id_usuario)
    }

    public fun selectEventoById(id_usuario: Long): List<ParticipantesDto> {
        return participantesQueries.selectParticipantesByUsuario(id_usuario).executeAsList().map { it.toDTO() }
    }


    fun getParticipanteByIdEventoAndUsuario(
        id_evento: Long,
        id_usuario: Long
    ): ParticipantesDto? {
        return participantesQueries.getParticipanteByIdEventoAndUsuario(
            id_evento,
            id_usuario
        ).executeAsOneOrNull()?.toDto()
    }
    public fun deleteAllParticipantes() {
        participantesQueries.deleteAllParticipantes()
    }
    fun deleteParticipantesByEventoId(idEvento: Long) {
        participantesQueries.deleteParticipantesByEventoId(idEvento)
    }

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


    fun Participantes.toDto() = ParticipantesDto(
        id_evento = id_evento,
        id_usuario = id_usuario)

    private fun Participantes.toDTO() = ParticipantesDto(
        id_evento = this.id_evento,
        id_usuario = this.id_usuario
    )
    fun Regalos.toDTO() = RegalosDto(
        id = id,
        id_evento = id_evento,
        nombre_regalo = nombre_regalo,
        descripcion = descripcion,
        precio_estimado = precio_estimado ?: 0.0,
        url_tienda = url_tienda,
        imagen = imagen,
        estado = estado ?: "disponible"
    )

}