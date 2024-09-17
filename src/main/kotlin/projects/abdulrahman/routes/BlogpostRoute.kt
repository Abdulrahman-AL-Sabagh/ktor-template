package projects.abdulrahman.routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.scope

import projects.abdulrahman.models.BlogPost
import projects.abdulrahman.models.BlogRepository

fun Route.blogpostRoute() {

    val repository by inject<BlogRepository>()
    route("blogpost") {
        get {
            call.respond(HttpStatusCode.OK, repository.getAll())
        }

        post {
            try {
                val blogpost = call.receive<BlogPost>()
                repository.create(blogpost)
                call.respond(HttpStatusCode.Created)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }


    }
}

