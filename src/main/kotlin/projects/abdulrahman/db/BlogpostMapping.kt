package projects.abdulrahman.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import projects.abdulrahman.models.BlogPost


object BlogpostTable : IntIdTable("blogpost") {
    val title = varchar("title", 255)
    val content = varchar("content", 255)
}

class BlogpostDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BlogpostDAO>(BlogpostTable)

    var title by BlogpostTable.title
    var content by BlogpostTable.content
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: BlogpostDAO): BlogPost {
    return BlogPost(
        title = dao.title,
        content = dao.content,
    )
}