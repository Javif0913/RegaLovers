package cl.usm.tel335.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class ParticipantesQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAllParticipantes(mapper: (id_evento: Long, id_usuario: Long) -> T):
      Query<T> = Query(-408_711_876, arrayOf("Participantes"), driver, "Participantes.sq",
      "selectAllParticipantes",
      "SELECT Participantes.id_evento, Participantes.id_usuario FROM Participantes") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!
    )
  }

  public fun selectAllParticipantes(): Query<Participantes> = selectAllParticipantes { id_evento,
      id_usuario ->
    Participantes(
      id_evento,
      id_usuario
    )
  }

  public fun <T : Any> selectParticipantesByEvento(id_evento: Long, mapper: (id_evento: Long,
      id_usuario: Long) -> T): Query<T> = SelectParticipantesByEventoQuery(id_evento) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!
    )
  }

  public fun selectParticipantesByEvento(id_evento: Long): Query<Participantes> =
      selectParticipantesByEvento(id_evento) { id_evento_, id_usuario ->
    Participantes(
      id_evento_,
      id_usuario
    )
  }

  public fun <T : Any> selectParticipantesByUsuario(id_usuario: Long, mapper: (id_evento: Long,
      id_usuario: Long) -> T): Query<T> = SelectParticipantesByUsuarioQuery(id_usuario) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!
    )
  }

  public fun selectParticipantesByUsuario(id_usuario: Long): Query<Participantes> =
      selectParticipantesByUsuario(id_usuario) { id_evento, id_usuario_ ->
    Participantes(
      id_evento,
      id_usuario_
    )
  }

  public fun <T : Any> getParticipanteByIdEventoAndUsuario(
    id_evento: Long,
    id_usuario: Long,
    mapper: (id_evento: Long, id_usuario: Long) -> T,
  ): Query<T> = GetParticipanteByIdEventoAndUsuarioQuery(id_evento, id_usuario) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!
    )
  }

  public fun getParticipanteByIdEventoAndUsuario(id_evento: Long, id_usuario: Long):
      Query<Participantes> = getParticipanteByIdEventoAndUsuario(id_evento, id_usuario) {
      id_evento_, id_usuario_ ->
    Participantes(
      id_evento_,
      id_usuario_
    )
  }

  public fun insertParticipante(id_evento: Long?, id_usuario: Long) {
    driver.execute(-1_293_369_845, """
        |INSERT INTO Participantes(id_evento, id_usuario)
        |VALUES (?, ?)
        """.trimMargin(), 2) {
          bindLong(0, id_evento)
          bindLong(1, id_usuario)
        }
    notifyQueries(-1_293_369_845) { emit ->
      emit("Participantes")
    }
  }

  public fun deleteParticipanteByIdEventoAndUsuario(id_evento: Long, id_usuario: Long) {
    driver.execute(-1_719_829_029,
        """DELETE FROM Participantes WHERE id_evento = ? AND id_usuario = ?""", 2) {
          bindLong(0, id_evento)
          bindLong(1, id_usuario)
        }
    notifyQueries(-1_719_829_029) { emit ->
      emit("Participantes")
    }
  }

  public fun deleteParticipantesByEventoId(id_evento: Long) {
    driver.execute(-1_928_202_947, """DELETE FROM  Participantes WHERE id_evento = ?""", 1) {
          bindLong(0, id_evento)
        }
    notifyQueries(-1_928_202_947) { emit ->
      emit("Participantes")
    }
  }

  public fun deleteAllParticipantes() {
    driver.execute(1_502_191_595, """DELETE FROM Participantes""", 0)
    notifyQueries(1_502_191_595) { emit ->
      emit("Participantes")
    }
  }

  private inner class SelectParticipantesByEventoQuery<out T : Any>(
    public val id_evento: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Participantes", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Participantes", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(697_024_977,
        """SELECT Participantes.id_evento, Participantes.id_usuario FROM Participantes WHERE id_evento = ?""",
        mapper, 1) {
      bindLong(0, id_evento)
    }

    override fun toString(): String = "Participantes.sq:selectParticipantesByEvento"
  }

  private inner class SelectParticipantesByUsuarioQuery<out T : Any>(
    public val id_usuario: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Participantes", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Participantes", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_376_594_418,
        """SELECT Participantes.id_evento, Participantes.id_usuario FROM Participantes WHERE id_usuario = ?""",
        mapper, 1) {
      bindLong(0, id_usuario)
    }

    override fun toString(): String = "Participantes.sq:selectParticipantesByUsuario"
  }

  private inner class GetParticipanteByIdEventoAndUsuarioQuery<out T : Any>(
    public val id_evento: Long,
    public val id_usuario: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Participantes", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Participantes", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(872_863_046, """
    |SELECT Participantes.id_evento, Participantes.id_usuario FROM Participantes
    |WHERE id_evento = ? AND id_usuario = ?
    """.trimMargin(), mapper, 2) {
      bindLong(0, id_evento)
      bindLong(1, id_usuario)
    }

    override fun toString(): String = "Participantes.sq:getParticipanteByIdEventoAndUsuario"
  }
}
