package cl.usm.tel335.database

import kotlin.Double
import kotlin.Long
import kotlin.String

public data class Regalos(
  public val id: Long,
  public val id_evento: Long,
  public val nombre_regalo: String,
  public val descripcion: String?,
  public val precio_estimado: Double?,
  public val url_tienda: String?,
  public val imagen: String?,
  public val estado: String?,
)
