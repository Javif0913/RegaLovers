package cl.usm.tel335.database

import kotlin.Long
import kotlin.String

public data class Eventos(
  public val id: Long,
  public val id_organizador: Long,
  public val nombre: String,
  public val descripcion: String?,
  public val lugar: String?,
  public val imagen: String?,
  public val tipo_evento: String?,
  public val estado: String?,
)
