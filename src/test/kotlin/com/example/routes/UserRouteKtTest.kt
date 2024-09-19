package com.example.routes

import com.example.configureApp
import com.example.models.User
import com.example.repositories.UserRepository
import com.example.util.lazyInject
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.test.BeforeTest
import kotlin.test.Test


@Tag("integration")
class UserRouteKtTest {
    val repository by lazyInject<UserRepository>()


    val testApp = TestApplication {
        application {
            configureApp(Config.forTest())
        }
    }


    private val appClient = testApp.createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    @BeforeTest
    fun setup() {
        runBlocking {
            appClient.get("/")
            repository.deleteAll()
        }
    }


    @Test
    fun `getAll users`(): Unit = runBlocking {
        appClient.get("/user").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(emptyList<List<User>>(), body<List<User>>())
        }

        repository.create(User("bob", "bob@example.com", "bobby"))
        val result2 = appClient.get("/user").body<List<User>>()
        assertEquals(repository.getAll().toString(), result2.toString())
    }

    @Test
    fun `given a user, when sending a post request, then check if the user is saved in the repository`(): Unit =
        testApplication {
            val newUser = User(name = "franz", email = "franz@example.com", password = "12354678")
            sendPostRequestToUser(newUser)
                .apply {
                    assertEquals(HttpStatusCode.Created, status)
                    assertTrue(repository.contains(newUser))
                }
        }

    @Test
    fun `given the same user twice, when sending 2 post requests, return Created then Conflict`() =
        testApplication {
            val newUser = User("bob", "bob@bob.bob", "bob")
            sendPostRequestToUser(newUser).apply { assertEquals(HttpStatusCode.Created, status) }
            sendPostRequestToUser(newUser).apply { assertEquals(HttpStatusCode.Conflict, status) }
        }


    @Test
    fun `given a new Created a User, when requesting the id of it, then return it`(): Unit = testApplication {
        val user = repository.create(User("bob", "bob", "bob"))
        appClient.get("/user/${user.id}").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(repository.getById(user.id), body<User>())

        }
    }


    @Test
    fun `when sending delete request for a user that doesn't exist, then return notfound`() = testApplication {
        appClient.delete("/user/1").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `given a user, when sending a delete request, then check that the user doesn't exist in the db anymore`() =
        testApplication {
            val user = repository.create(User("bob", "bob@bob.com", "bob"))
            appClient.delete("/user/${user.id}").apply {
                assertEquals(HttpStatusCode.Accepted, status)
                assertFalse(repository.contains(user))
            }
        }

    @Test
    fun `when sending a delete request with invalid id, then return BadRequest`() = testApplication {
        appClient.delete("/user/hi").apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    @Test
    fun `when getting the user from the api, then return not found`() = testApplication {
        appClient.get("/user/33333333").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }


    private fun sendPostRequestToUser(body: User): HttpResponse = runBlocking {
        return@runBlocking appClient.post("/user") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }
}




