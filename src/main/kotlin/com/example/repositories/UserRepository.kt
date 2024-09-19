package com.example.repositories

import com.example.db.UserDAO
import com.example.db.UserTable
import com.example.db.daoToModel
import com.example.models.User
import com.example.util.suspendTransaction
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll

interface UserRepository {
    suspend fun getAll(): List<User>
    suspend fun getById(id: Int): User?
    suspend fun contains(user: User): Boolean
    suspend fun create(user: User): User
    suspend fun update(user: User): Unit
    suspend fun delete(user: User): Boolean
    suspend fun getManyByName(): List<User>
    suspend fun deleteAll(): Unit
    suspend fun count(): Long
}

class UserRepositoryImpl : UserRepository {

    override suspend fun getAll(): List<User> = suspendTransaction {
        UserDAO.all().map(::daoToModel)
    }

    override suspend fun getById(id: Int): User? = suspendTransaction {
        val searchResult = UserDAO.findById(id) ?: return@suspendTransaction null
        return@suspendTransaction daoToModel(searchResult)
    }

    override suspend fun contains(user: User): Boolean = suspendTransaction {
        return@suspendTransaction UserTable.selectAll().where(UserTable.email.eq(user.email)).count() > 0
    }

    override suspend fun create(user: User): User = suspendTransaction {
        return@suspendTransaction daoToModel(UserDAO.new {
            name = user.name
            email = user.email
            password = user.password
        })


    }


    override suspend fun update(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(user: User): Boolean = suspendTransaction {
        val userExists = runBlocking { return@runBlocking getById(user.id) != null }
        if (!userExists) {
            return@suspendTransaction false
        }
        UserTable.deleteWhere { UserTable.id.eq(user.id) }

        return@suspendTransaction true
    }

    override suspend fun getManyByName(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll(): Unit = suspendTransaction { UserTable.deleteAll() }
    override suspend fun count(): Long = suspendTransaction { UserDAO.count() }


}

