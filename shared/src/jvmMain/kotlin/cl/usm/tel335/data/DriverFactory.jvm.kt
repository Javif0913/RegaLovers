package cl.usm.tel335.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import cl.usm.tel335.database.Database
import java.io.File

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val dbFile = File("build/eventos.db")  // Puedes cambiar la ruta si quieres
        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")

        if (!dbFile.exists()) {
            Database.Schema.create(driver)
        }

        return driver
    }
}
