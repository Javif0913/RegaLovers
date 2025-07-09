package cl.usm.tel335.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class UsuariosQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectUsuarioById(id: Long, mapper: (
    id: Long,
    nickname: String,
    email: String,
    contrasena_hash: String,
    fecha_registro: Long,
    avatar_imagen: String?,
  ) -> T): Query<T> = SelectUsuarioByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)
    )
  }

  public fun selectUsuarioById(id: Long): Query<Usuarios> = selectUsuarioById(id) { id_, nickname,
      email, contrasena_hash, fecha_registro, avatar_imagen ->
    Usuarios(
      id_,
      nickname,
      email,
      contrasena_hash,
      fecha_registro,
      avatar_imagen
    )
  }

  public fun <T : Any> selectUsuarioByNickname(nickname: String, mapper: (
    id: Long,
    nickname: String,
    email: String,
    contrasena_hash: String,
    fecha_registro: Long,
    avatar_imagen: String?,
  ) -> T): Query<T> = SelectUsuarioByNicknameQuery(nickname) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)
    )
  }

  public fun selectUsuarioByNickname(nickname: String): Query<Usuarios> =
      selectUsuarioByNickname(nickname) { id, nickname_, email, contrasena_hash, fecha_registro,
      avatar_imagen ->
    Usuarios(
      id,
      nickname_,
      email,
      contrasena_hash,
      fecha_registro,
      avatar_imagen
    )
  }

  public fun <T : Any> selectUsuarioByEmail(email: String, mapper: (
    id: Long,
    nickname: String,
    email: String,
    contrasena_hash: String,
    fecha_registro: Long,
    avatar_imagen: String?,
  ) -> T): Query<T> = SelectUsuarioByEmailQuery(email) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)
    )
  }

  public fun selectUsuarioByEmail(email: String): Query<Usuarios> = selectUsuarioByEmail(email) {
      id, nickname, email_, contrasena_hash, fecha_registro, avatar_imagen ->
    Usuarios(
      id,
      nickname,
      email_,
      contrasena_hash,
      fecha_registro,
      avatar_imagen
    )
  }

  public fun <T : Any> selectAllUsuarios(mapper: (
    id: Long,
    nickname: String,
    email: String,
    contrasena_hash: String,
    fecha_registro: Long,
    avatar_imagen: String?,
  ) -> T): Query<T> = Query(-1_978_440_536, arrayOf("Usuarios"), driver, "Usuarios.sq",
      "selectAllUsuarios",
      "SELECT Usuarios.id, Usuarios.nickname, Usuarios.email, Usuarios.contrasena_hash, Usuarios.fecha_registro, Usuarios.avatar_imagen FROM Usuarios") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!,
      cursor.getString(3)!!,
      cursor.getLong(4)!!,
      cursor.getString(5)
    )
  }

  public fun selectAllUsuarios(): Query<Usuarios> = selectAllUsuarios { id, nickname, email,
      contrasena_hash, fecha_registro, avatar_imagen ->
    Usuarios(
      id,
      nickname,
      email,
      contrasena_hash,
      fecha_registro,
      avatar_imagen
    )
  }

  public fun insertUsuario(
    nickname: String,
    email: String,
    contrasena_hash: String,
    fecha_registro: Long,
    avatar_imagen: String?,
  ) {
    driver.execute(205_515_955, """
        |INSERT INTO Usuarios(nickname, email, contrasena_hash, fecha_registro, avatar_imagen)
        |    VALUES (?, ?, ?, ?, ?)
        """.trimMargin(), 5) {
          bindString(0, nickname)
          bindString(1, email)
          bindString(2, contrasena_hash)
          bindLong(3, fecha_registro)
          bindString(4, avatar_imagen)
        }
    notifyQueries(205_515_955) { emit ->
      emit("Usuarios")
    }
  }

  public fun updateContrasena(contrasena_hash: String, id: Long) {
    driver.execute(-1_061_278_127, """UPDATE Usuarios SET contrasena_hash = ? WHERE id = ?""", 2) {
          bindString(0, contrasena_hash)
          bindLong(1, id)
        }
    notifyQueries(-1_061_278_127) { emit ->
      emit("Usuarios")
    }
  }

  public fun deleteUsuarioById(id: Long) {
    driver.execute(1_334_803_699, """DELETE FROM Usuarios WHERE id = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(1_334_803_699) { emit ->
      emit("Usuarios")
    }
  }

  public fun deleteAllUsuarios() {
    driver.execute(862_593_177, """DELETE FROM Usuarios""", 0)
    notifyQueries(862_593_177) { emit ->
      emit("Usuarios")
    }
  }

  private inner class SelectUsuarioByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Usuarios", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Usuarios", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-1_506_230_014,
        """SELECT Usuarios.id, Usuarios.nickname, Usuarios.email, Usuarios.contrasena_hash, Usuarios.fecha_registro, Usuarios.avatar_imagen FROM Usuarios WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Usuarios.sq:selectUsuarioById"
  }

  private inner class SelectUsuarioByNicknameQuery<out T : Any>(
    public val nickname: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Usuarios", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Usuarios", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-677_604_587,
        """SELECT Usuarios.id, Usuarios.nickname, Usuarios.email, Usuarios.contrasena_hash, Usuarios.fecha_registro, Usuarios.avatar_imagen FROM Usuarios WHERE nickname = ?""",
        mapper, 1) {
      bindString(0, nickname)
    }

    override fun toString(): String = "Usuarios.sq:selectUsuarioByNickname"
  }

  private inner class SelectUsuarioByEmailQuery<out T : Any>(
    public val email: String,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Usuarios", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Usuarios", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(1_716_632_149,
        """SELECT Usuarios.id, Usuarios.nickname, Usuarios.email, Usuarios.contrasena_hash, Usuarios.fecha_registro, Usuarios.avatar_imagen FROM Usuarios WHERE email = ?""",
        mapper, 1) {
      bindString(0, email)
    }

    override fun toString(): String = "Usuarios.sq:selectUsuarioByEmail"
  }
}
