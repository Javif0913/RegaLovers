package cl.usm.tel335.database

import kotlin.Long
import kotlin.String

public data class Usuarios(
  public val id: Long,
  public val nickname: String,
  public val email: String,
  public val contrasena_hash: String,
  public val fecha_registro: Long,
  public val avatar_imagen: String?,
)
