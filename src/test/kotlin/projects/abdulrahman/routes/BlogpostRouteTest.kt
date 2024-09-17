package projects.abdulrahman.routes

import io.ktor.client.call.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import projects.abdulrahman.models.BlogFakeRepository
import projects.abdulrahman.module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.testcontainers.junit.jupiter.Testcontainers
import projects.abdulrahman.AbstractDatabaseTest
import projects.abdulrahman.models.BlogPost
import projects.abdulrahman.models.BlogRepository
import projects.abdulrahman.models.BlogRepositoryImpl
import projects.abdulrahman.plugins.configureRouting
import projects.abdulrahman.plugins.configureSerialization

@Testcontainers
class BlogpostRouteTest : AbstractDatabaseTest() {

    private val blogpostRepository = BlogRepositoryImpl()

    private val testApp = TestApplication {
        install(Koin) {
            modules(
                module {
                    single<BlogRepository> { blogpostRepository }
                }
            )
        }
        application {
            configureSerialization()
            configureRouting()
        }
    }
    private val client = testApp.createClient {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json()
        }
    }


    @Test
    fun getAllBlogPosts() = runBlocking {


        val request = client.get("/blogpost")
        assertEquals(HttpStatusCode.OK, request.status)
        assertEquals(blogpostRepository.getAll(), request.body<List<BlogPost>>())
    }

    @Test
    fun createABlogPost() = testApplication {
        application {
            module()

        }
        val repository = BlogFakeRepository
        routing {
            blogpostRoute()
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val blogpost = BlogPost("Hello world", "bla to the bla bla bla")
        client.post("/blogpost") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(blogpost)
        }.apply {
            assertEquals(HttpStatusCode.Created, status)
            assertTrue(repository.getAll().contains(blogpost))
        }

    }

    @Test
    fun `badRequestIfObjectIsn'tABlogPost`() = testApplication {
        application { module() }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }
        val request = client.post("/blogpost") {
            contentType(ContentType.Application.Json)
            setBody("bar" to "booo")
        }
        assertEquals(HttpStatusCode.BadRequest, request.status)

    }
}