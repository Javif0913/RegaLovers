package cl.usm.tel335.data

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory {
    fun createDriver() : SqlDriver
}