package cl.usm.tel335.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Double
import kotlin.Long
import kotlin.String

public class RegalosQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectRegaloById(id: Long, mapper: (
    id: Long,
    id_evento: Long,
    nombre_regalo: String,
    descripcion: String?,
    precio_estimado: Double?,
    url_tienda: String?,
    imagen: String?,
    estado: String?,
  ) -> T): Query<T> = SelectRegaloByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getDouble(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7)
    )
  }

  public fun selectRegaloById(id: Long): Query<Regalos> = selectRegaloById(id) { id_, id_evento,
      nombre_regalo, descripcion, precio_estimado, url_tienda, imagen, estado ->
    Regalos(
      id_,
      id_evento,
      nombre_regalo,
      descripcion,
      precio_estimado,
      url_tienda,
      imagen,
      estado
    )
  }

  public fun <T : Any> selectAllRegalos(mapper: (
    id: Long,
    id_evento: Long,
    nombre_regalo: String,
    descripcion: String?,
    precio_estimado: Double?,
    url_tienda: String?,
    imagen: String?,
    estado: String?,
  ) -> T): Query<T> = Query(1_932_072_640, arrayOf("Regalos"), driver, "Regalos.sq",
      "selectAllRegalos",
      "SELECT Regalos.id, Regalos.id_evento, Regalos.nombre_regalo, Regalos.descripcion, Regalos.precio_estimado, Regalos.url_tienda, Regalos.imagen, Regalos.estado FROM Regalos") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getDouble(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7)
    )
  }

  public fun selectAllRegalos(): Query<Regalos> = selectAllRegalos { id, id_evento, nombre_regalo,
      descripcion, precio_estimado, url_tienda, imagen, estado ->
    Regalos(
      id,
      id_evento,
      nombre_regalo,
      descripcion,
      precio_estimado,
      url_tienda,
      imagen,
      estado
    )
  }

  public fun <T : Any> selectRegalosByEvento(id_evento: Long, mapper: (
    id: Long,
    id_evento: Long,
    nombre_regalo: String,
    descripcion: String?,
    precio_estimado: Double?,
    url_tienda: String?,
    imagen: String?,
    estado: String?,
  ) -> T): Query<T> = SelectRegalosByEventoQuery(id_evento) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getLong(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3),
      cursor.getDouble(4),
      cursor.getString(5),
      cursor.getString(6),
      cursor.getString(7)
    )
  }

  public fun selectRegalosByEvento(id_evento: Long): Query<Regalos> =
      selectRegalosByEvento(id_evento) { id, id_evento_, nombre_regalo, descripcion,
      precio_estimado, url_tienda, imagen, estado ->
    Regalos(
      id,
      id_evento_,
      nombre_regalo,
      descripcion,
      precio_estimado,
      url_tienda,
      imagen,
      estado
    )
  }

  public fun insertRegalo(
    id_evento: Long,
    nombre_regalo: String,
    descripcion: String?,
    precio_estimado: Double?,
    url_tienda: String?,
    imagen: String?,
    estado: String?,
  ) {
    driver.execute(276_298_059, """
        |INSERT INTO Regalos(id_evento, nombre_regalo, descripcion, precio_estimado, url_tienda, imagen, estado)
        |VALUES (?, ?, ?, ?, ?, ?, ?)
        """.trimMargin(), 7) {
          bindLong(0, id_evento)
          bindString(1, nombre_regalo)
          bindString(2, descripcion)
          bindDouble(3, precio_estimado)
          bindString(4, url_tienda)
          bindString(5, imagen)
          bindString(6, estado)
        }
    notifyQueries(276_298_059) { emit ->
      emit("Regalos")
    }
  }

  public fun updateRegalo(
    nombre_regalo: String,
    descripcion: String?,
    precio_estimado: Double?,
    url_tienda: String?,
    imagen: String?,
    estado: String?,
    id: Long,
  ) {
    driver.execute(-1_418_026_149,
        """UPDATE Regalos SET nombre_regalo = ?, descripcion = ?, precio_estimado = ?, url_tienda = ?, imagen = ?, estado = ? WHERE id = ?""",
        7) {
          bindString(0, nombre_regalo)
          bindString(1, descripcion)
          bindDouble(2, precio_estimado)
          bindString(3, url_tienda)
          bindString(4, imagen)
          bindString(5, estado)
          bindLong(6, id)
        }
    notifyQueries(-1_418_026_149) { emit ->
      emit("Regalos")
    }
  }

  public fun deleteRegaloById(id: Long) {
    driver.execute(1_315_989_551, """DELETE FROM Regalos WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(1_315_989_551) { emit ->
      emit("Regalos")
    }
  }

  public fun deleteAllRegalos() {
    driver.execute(84_056_239, """DELETE FROM Regalos""", 0)
    notifyQueries(84_056_239) { emit ->
      emit("Regalos")
    }
  }

  private inner class SelectRegaloByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Regalos", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Regalos", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_130_961_344,
        """SELECT Regalos.id, Regalos.id_evento, Regalos.nombre_regalo, Regalos.descripcion, Regalos.precio_estimado, Regalos.url_tienda, Regalos.imagen, Regalos.estado FROM Regalos WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Regalos.sq:selectRegaloById"
  }

  private inner class SelectRegalosByEventoQuery<out T : Any>(
    public val id_evento: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Regalos", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Regalos", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_705_699_409,
        """SELECT Regalos.id, Regalos.id_evento, Regalos.nombre_regalo, Regalos.descripcion, Regalos.precio_estimado, Regalos.url_tienda, Regalos.imagen, Regalos.estado FROM Regalos WHERE id_evento = ?""",
        mapper, 1) {
      bindLong(0, id_evento)
    }

    override fun toString(): String = "Regalos.sq:selectRegalosByEvento"
  }
}
