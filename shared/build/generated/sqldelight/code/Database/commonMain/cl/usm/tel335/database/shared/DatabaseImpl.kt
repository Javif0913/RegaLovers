package cl.usm.tel335.database.shared

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import cl.usm.tel335.database.Database
import cl.usm.tel335.database.EventosQueries
import cl.usm.tel335.database.ParticipantesQueries
import cl.usm.tel335.database.QuestionQueries
import cl.usm.tel335.database.RegalosQueries
import cl.usm.tel335.database.UsuariosQueries
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<Database>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = DatabaseImpl.Schema

internal fun KClass<Database>.newInstance(driver: SqlDriver): Database = DatabaseImpl(driver)

private class DatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver), Database {
  override val eventosQueries: EventosQueries = EventosQueries(driver)

  override val participantesQueries: ParticipantesQueries = ParticipantesQueries(driver)

  override val questionQueries: QuestionQueries = QuestionQueries(driver)

  override val regalosQueries: RegalosQueries = RegalosQueries(driver)

  override val usuariosQueries: UsuariosQueries = UsuariosQueries(driver)

  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> {
      driver.execute(null, """
          |CREATE TABLE Eventos (
          |  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |  id_organizador INTEGER NOT NULL,
          |  nombre TEXT NOT NULL,
          |  descripcion TEXT,
          |  lugar TEXT,
          |  imagen TEXT,
          |  tipo_evento TEXT,
          |  estado TEXT
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE Participantes (
          |
          |  id_evento INTEGER NOT NULL PRIMARY KEY,
          |  id_usuario INTEGER NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE Question (
          |    id INTEGER NOT NULL PRIMARY KEY,
          |    text TEXT NOT NULL,
          |    category TEXT NOT NULL
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE Regalos (
          |  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |  id_evento INTEGER NOT NULL,
          |  nombre_regalo TEXT NOT NULL,
          |  descripcion TEXT,
          |  precio_estimado REAL, -- DECIMAL se mapea a REAL en SQLite
          |  url_tienda TEXT,
          |  imagen TEXT,
          |  estado TEXT
          |)
          """.trimMargin(), 0)
      driver.execute(null, """
          |CREATE TABLE Usuarios (
          |      id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
          |      nickname TEXT NOT NULL UNIQUE, -- Asumo que el nickname debe ser único
          |      email TEXT NOT NULL UNIQUE,   -- El email debe ser único
          |      contrasena_hash TEXT NOT NULL,
          |      fecha_registro INTEGER NOT NULL, -- DATETIME se mapea a INTEGER (Unix timestamp en milisegundos) en SQLite
          |      avatar_imagen TEXT
          |    )
          """.trimMargin(), 0)
      driver.execute(null, """
          |INSERT INTO Eventos(id_organizador, nombre, descripcion, lugar, imagen, tipo_evento, estado) VALUES
          |(1, 'Concierto de Rock', 'Un concierto épico de rock con bandas locales.', 'Estadio Nacional', 'https://example.com/concierto.png', 'Música', 'Activo'),
          |(2, 'Conferencia de Tecnología', 'Charlas y talleres sobre las últimas tendencias tecnológicas.', 'Centro de Convenciones', 'https://example.com/tech_conf.png', 'Conferencia', 'Activo'),
          |(1, 'Festival Gastronómico', 'Prueba la mejor comida local e internacional.', 'Parque Central', 'https://example.com/gastronomia.png', 'Comida', 'Pendiente'),
          |(1, 'Cumpleaños', 'Por favor ben :(', 'Estadio Nacional', 'https://example.com/concierto.png', 'Música', 'Activo')
          """.trimMargin(), 0)
      driver.execute(null, """
          |INSERT INTO Participantes(id_evento, id_usuario) VALUES
          |(1, 1), -- Usuario 1 participa en Evento 1
          |(1, 2), -- Usuario 2 participa en Evento 1
          |(2, 1)
          """.trimMargin(), 0)
      driver.execute(null, """
          |INSERT INTO Question(id, text, category) VALUES
          |(1, '¿Cuál es la capital de Francia?', 'Geografía'),
          |(2, '¿Quién escribió Cien años de soledad?', 'Literatura'),
          |(3, '¿Cuál es el resultado de 9 x 7?', 'Matemáticas')
          """.trimMargin(), 0)
      driver.execute(null, """
          |INSERT INTO Regalos(id_evento, nombre_regalo, descripcion, precio_estimado, url_tienda, imagen, estado) VALUES
          |(1, 'Guitarra Eléctrica', 'Guitarra para coleccionistas.', 500.00, 'https://tienda.com/guitarra', 'https://example.com/guitarra.png', 'Disponible'),
          |(1, 'Entradas VIP', 'Entradas para el área VIP del concierto.', 150.00, 'https://tienda.com/entradas', NULL, 'Disponible'),
          |(2, 'Libro de Programación', 'Un libro sobre Kotlin Multiplatform.', 45.99, 'https://libreria.com/kotlin', 'https://example.com/libro.png', 'Reservado')
          """.trimMargin(), 0)
      driver.execute(null, """
          |INSERT INTO Usuarios(nickname, email, contrasena_hash, fecha_registro, avatar_imagen) VALUES
          |    ('usuario1', 'usuario1@example.com', 'hash123_user1', 1678886400000, NULL),
          |    ('admin', 'admin@example.com', 'admin_hash_secure', 1678972800000, NULL),
          |    ('maria_p', 'maria.perez@example.com', 'maria_pass_hash', 1679059200000, NULL)
          """.trimMargin(), 0)
      return QueryResult.Unit
    }

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
