package cl.usm.tel335.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsuariosDto(
    @SerialName("id")
    val id: Long = -1,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("email")
    val email: String,
    @SerialName("contrasena_hash")
    val contrasena_hash: String,
    @SerialName("fecha_registro")
    val fecha_registro: Long,
    @SerialName("avatar_imagen")
    val avatar_imagen: String?
)

@Serializable
data class EventosDto(
    @SerialName("id")
    val id: Long = -1,
    @SerialName("id_organizador")
    val id_organizador: Long,
    @SerialName("nombre")
    val nombre: String,
    @SerialName("descripcion")
    val descripcion: String?,
    @SerialName("lugar")
    val lugar: String?,
    @SerialName("imagen")
    val imagen: String?,
    @SerialName("tipo_evento")
    val tipo_evento: String?,
    @SerialName("estado")
    val estado: String?
)

@Serializable
data class RegalosDto(
    @SerialName("id")
    val id: Long = -1,
    @SerialName("id_evento")
    val id_evento: Long,
    @SerialName("nombre_regalo")
    val nombre_regalo: String,
    @SerialName("descripcion")
    val descripcion: String?,
    @SerialName("precio_estimado")
    val precio_estimado: Double?,
    @SerialName("url_tienda")
    val url_tienda: String?,
    @SerialName("imagen")
    val imagen: String?,
    @SerialName("estado")
    val estado: String?
)

@Serializable
data class ParticipantesDto(
    @SerialName("id_evento")
    val id_evento: Long,
    @SerialName("id_usuario")
    val id_usuario: Long
)
