package projects.abdulrahman.models

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import projects.abdulrahman.db.BlogpostDAO
import projects.abdulrahman.db.daoToModel
import projects.abdulrahman.db.suspendTransaction


val blogDataModule = module {
    single { BlogRepositoryImpl() }
}


class BlogRepositoryImpl : BlogRepository {
    override suspend fun getAll(): List<BlogPost> = suspendTransaction { BlogpostDAO.all().map(::daoToModel) }


    override suspend fun create(blogPost: BlogPost): Unit = suspendTransaction {
        BlogpostDAO.new {
            title = blogPost.title
            content = blogPost.content
        }
    }

}