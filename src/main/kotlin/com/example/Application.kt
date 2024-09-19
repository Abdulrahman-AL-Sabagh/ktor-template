package com.example

import Config
import com.example.ext.configureCors
import com.example.ext.configureDatabases
import com.example.ext.configureKoin
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureApp(
            Config(
                "postgres",
                "postgres",
                "postgres",
                "jdbc:postgresql://localhost:5432/postgres"

            )
        )
    }.start(wait = true)
}

fun Application.configureApp(config: Config) {

    val db = configureDatabases(config)
    configureSerialization()
    configureHTTP()
    configureRouting()
    configureKoin()
    configureCors()
}

