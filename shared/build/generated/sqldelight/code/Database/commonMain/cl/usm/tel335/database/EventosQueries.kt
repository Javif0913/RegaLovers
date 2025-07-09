package cl.usm.tel335.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class EventosQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectEventoById(id: Long, mapper: (
    id: Long,
    id_organizador: Long,
    nombre: String,
    descripcion: String?,
    lugar: String?,
    imagen: String?,
    tipo_evento: String?,
    estado: String?,
  ) -> T): Query<T> = SelectEventoByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7)
    )
  }

  public fun selectEventoById(id: Long): Query<Eventos> = selectEventoById(id) { id_,
      id_organizador, nombre, descripcion, lugar, imagen, tipo_evento, estado ->
    Eventos(
      id_,
      id_organizador,
      nombre,
      descripcion,
      lugar,
      imagen,
      tipo_evento,
      estado
    )
  }

  public fun <T : Any> selectAllEventos(mapper: (
    id: Long,
    id_organizador: Long,
    nombre: String,
    descripcion: String?,
    lugar: String?,
    imagen: String?,
    tipo_evento: String?,
    estado: String?,
  ) -> T): Query<T> = Query(737_504_118, arrayOf("Eventos"), driver, "Eventos.sq",
      "selectAllEventos",
      "SELECT Eventos.id, Eventos.id_organizador, Eventos.nombre, Eventos.descripcion, Eventos.lugar, Eventos.imagen, Eventos.tipo_evento, Eventos.estado FROM Eventos") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7)
    )
  }

  public fun selectAllEventos(): Query<Eventos> = selectAllEventos { id, id_organizador, nombre,
      descripcion, lugar, imagen, tipo_evento, estado ->
    Eventos(
      id,
      id_organizador,
      nombre,
      descripcion,
      lugar,
      imagen,
      tipo_evento,
      estado
    )
  }

  public fun <T : Any> selectEventosByOrganizador(id_organizador: Long, mapper: (
    id: Long,
    id_organizador: Long,
    nombre: String,
    descripcion: String?,
    lugar: String?,
    imagen: String?,
    tipo_evento: String?,
    estado: String?,
  ) -> T): Query<T> = SelectEventosByOrganizadorQuery(id_organizador) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getString(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7)
    )
  }

  public fun selectEventosByOrganizador(id_organizador: Long): Query<Eventos> =
      selectEventosByOrganizador(id_organizador) { id, id_organizador_, nombre, descripcion, lugar,
      imagen, tipo_evento, estado ->
    Eventos(
      id,
      id_organizador_,
      nombre,
      descripcion,
      lugar,
      imagen,
      tipo_evento,
      estado
    )
  }

  public fun insertEvento(
    id_organizador: Long,
    nombre: String,
    descripcion: String?,
    lugar: String?,
    imagen: String?,
    tipo_evento: String?,
    estado: String?,
  ) {
    driver.execute(1_022_194_667, """
        |INSERT INTO Eventos(id_organizador, nombre, descripcion, lugar, imagen, tipo_evento, estado)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindLong(0, id_organizador)
          bindString(1, nombre)
          bindString(2, descripcion)
          bindString(3, lugar)
          bindString(4, imagen)
          bindString(5, tipo_evento)
          bindString(6, estado)
        }
    notifyQueries(1_022_194_667) { emit ->
      emit("Eventos")
    }
  }

  public fun updateEvento(
    nombre: String,
    descripcion: String?,
    lugar: String?,
    imagen: String?,
    tipo_evento: String?,
    estado: String?,
    id: Long,
  ) {
    driver.execute(-672_129_541,
        """UPDATE Eventos SET nombre = ?, descripcion = ?, lugar = ?, imagen = ?, tipo_evento = ?, estado = ? WHERE id = ?""",
        7) {
          bindString(0, nombre)
          bindString(1, descripcion)
          bindString(2, lugar)
          bindString(3, imagen)
          bindString(4, tipo_evento)
          bindString(5, estado)
          bindLong(6, id)
        }
    notifyQueries(-672_129_541) { emit ->
      emit("Eventos")
    }
  }

  public fun deleteEventoById(id: Long) {
    driver.execute(-127_429_937, """DELETE FROM Eventos WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(-127_429_937) { emit ->
      emit("Eventos")
    }
  }

  public fun deleteAllEventos() {
    driver.execute(-1_110_512_283, """DELETE FROM Eventos""", 0)
    notifyQueries(-1_110_512_283) { emit ->
      emit("Eventos")
    }
  }

  private inner class SelectEventoByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Eventos", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Eventos", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_720_586_464,
        """SELECT Eventos.id, Eventos.id_organizador, Eventos.nombre, Eventos.descripcion, Eventos.lugar, Eventos.imagen, Eventos.tipo_evento, Eventos.estado FROM Eventos WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Eventos.sq:selectEventoById"
  }

  private inner class SelectEventosByOrganizadorQuery<out T : Any>(
    public val id_organizador: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Eventos", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Eventos", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(431_893_548,
        """SELECT Eventos.id, Eventos.id_organizador, Eventos.nombre, Eventos.descripcion, Eventos.lugar, Eventos.imagen, Eventos.tipo_evento, Eventos.estado FROM Eventos WHERE id_organizador = ?""",
        mapper, 1) {
      bindLong(0, id_organizador)
    }

    override fun toString(): String = "Eventos.sq:selectEventosByOrganizador"
  }
}
