package cl.usm.tel335.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    @SerialName("id")
    val id: Long = -1,
    @SerialName("text")
    val text: String,
    @SerialName("category")
    val category: String //TODO: crear clase category
)

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
    val fecha_registro: Long, // Mapea a Long para el timestamp
    @SerialName("avatar_imagen")
    val avatar_imagen: String? // Puede ser nulo
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
    val descripcion: String?, // Puede ser nulo en la base de datos
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
    val precio_estimado: Double?, // DECIMAL se mapea a Double en Kotlin
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

@Serializable
data class LoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("contrasena")
    val contrasena: String,
)
