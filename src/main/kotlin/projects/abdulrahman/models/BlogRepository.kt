package projects.abdulrahman.models

import projects.abdulrahman.db.BlogpostDAO
import projects.abdulrahman.db.daoToModel
import projects.abdulrahman.db.suspendTransaction

interface BlogRepository {
    suspend fun getAll(): List<BlogPost>

    suspend fun create(blogPost: BlogPost)
}