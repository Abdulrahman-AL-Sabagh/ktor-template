package com.example.routes

import com.example.models.User
import com.example.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {
    val userRepository by inject<UserRepository>()
    route("/user") {
        get {
            call.respond(HttpStatusCode.OK, userRepository.getAll())
        }
        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val user = userRepository.getById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            call.respond(HttpStatusCode.OK, user)

        }
        post {
            try {
                val user = call.receive<User>()
                if (userRepository.contains(user)) {
                    return@post call.respond(HttpStatusCode.Conflict)
                }
                userRepository.create(user)

                call.respond(HttpStatusCode.Created, user)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest)
            }

        }
        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val user = userRepository.getById(id) ?: return@delete call.respond(HttpStatusCode.NotFound)
            userRepository.delete(user)
            call.respond(HttpStatusCode.Accepted, user)
        }

    }
}