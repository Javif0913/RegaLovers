package cl.usm.tel335.database

import kotlin.Long
import kotlin.String

public data class Question(
  public val id: Long,
  public val text: String,
  public val category: String,
)
