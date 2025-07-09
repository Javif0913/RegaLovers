package cl.usm.tel335.database

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import cl.usm.tel335.database.shared.newInstance
import cl.usm.tel335.database.shared.schema
import kotlin.Unit

public interface Database : Transacter {
  public val eventosQueries: EventosQueries

  public val participantesQueries: ParticipantesQueries

  public val questionQueries: QuestionQueries

  public val regalosQueries: RegalosQueries

  public val usuariosQueries: UsuariosQueries

  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = Database::class.schema

    public operator fun invoke(driver: SqlDriver): Database = Database::class.newInstance(driver)
  }
}
