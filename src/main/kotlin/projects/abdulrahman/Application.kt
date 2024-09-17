package projects.abdulrahman

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.routing.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import projects.abdulrahman.models.BlogFakeRepository
import projects.abdulrahman.models.BlogRepository
import projects.abdulrahman.models.BlogRepositoryImpl
import projects.abdulrahman.models.blogDataModule
import projects.abdulrahman.plugins.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}


fun Application.module() {

    configureSerialization()
    configureDatabases()
    configureRouting()
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }
    install(Koin) {
        modules(blogDataModule)
    }

}
