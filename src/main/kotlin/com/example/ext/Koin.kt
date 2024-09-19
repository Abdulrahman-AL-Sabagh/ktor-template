package com.example.ext

import com.example.repositories.UserRepository
import com.example.repositories.UserRepositoryImpl
import io.ktor.server.application.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger


fun Application.configureKoin() {
    val modules = module {
        single<UserRepository> { UserRepositoryImpl() }
    }
    install(Koin) {
        slf4jLogger()
        modules(modules)
    }

}