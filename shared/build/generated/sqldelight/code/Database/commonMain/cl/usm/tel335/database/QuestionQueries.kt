package cl.usm.tel335.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class QuestionQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAll(mapper: (
    id: Long,
    text: String,
    category: String,
  ) -> T): Query<T> = Query(535_113_026, arrayOf("Question"), driver, "Question.sq", "selectAll",
      "SELECT Question.id, Question.text, Question.category FROM Question") { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!
    )
  }

  public fun selectAll(): Query<Question> = selectAll { id, text, category ->
    Question(
      id,
      text,
      category
    )
  }

  public fun <T : Any> selectById(id: Long, mapper: (
    id: Long,
    text: String,
    category: String,
  ) -> T): Query<T> = SelectByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getString(2)!!
    )
  }

  public fun selectById(id: Long): Query<Question> = selectById(id) { id_, text, category ->
    Question(
      id_,
      text,
      category
    )
  }

  public fun insertQuestion(
    id: Long?,
    text: String,
    category: String,
  ) {
    driver.execute(-1_395_841_630, """
        |INSERT INTO Question(id, text, category)
        |VALUES (?, ?, ?)
        """.trimMargin(), 3) {
          bindLong(0, id)
          bindString(1, text)
          bindString(2, category)
        }
    notifyQueries(-1_395_841_630) { emit ->
      emit("Question")
    }
  }

  public fun deleteAll() {
    driver.execute(1_892_379_187, """DELETE FROM Question""", 0)
    notifyQueries(1_892_379_187) { emit ->
      emit("Question")
    }
  }

  public fun deleteById(id: Long) {
    driver.execute(-1_465_746_048, """
        |DELETE FROM Question
        |WHERE id = ?
        """.trimMargin(), 1) {
          bindLong(0, id)
        }
    notifyQueries(-1_465_746_048) { emit ->
      emit("Question")
    }
  }

  private inner class SelectByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("Question", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("Question", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-591_324_079, """
    |SELECT Question.id, Question.text, Question.category FROM Question
    |WHERE id = ?
    """.trimMargin(), mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Question.sq:selectById"
  }
}
