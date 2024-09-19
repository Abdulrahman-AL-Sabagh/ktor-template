package com.example.ext

import Config
import com.example.db.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


fun configureDatabases(config: Config) {

    val database = Database.connect(
        url = config.databaseUrl,
        user = config.databaseUser,
        password = config.databasePassword,
        driver = "org.postgresql.Driver"
    )
    transaction {
        SchemaUtils.create(UserTable)
    }


}





