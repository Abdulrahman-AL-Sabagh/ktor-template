package projects.abdulrahman.models

import java.time.LocalDateTime

object BlogFakeRepository : BlogRepository {
    private val blogs = mutableListOf(
        BlogPost(
            title = "First blog", content = """
        Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
        sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
         At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren,
         o sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
         sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.
         At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, 
        no sea takimata sanctus est Lorem ipsum dolor sit amet.   
        """.trimIndent()
        )
    )

    override suspend fun getAll(): List<BlogPost> = blogs
    override suspend fun create(blogPost: BlogPost) {
        blogs.add(blogPost)
    }
}