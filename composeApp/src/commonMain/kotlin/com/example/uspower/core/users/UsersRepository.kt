package com.example.uspower.core.users

import com.example.uspower.models.Role
import com.example.uspower.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    val usersFLow: Flow<List<User>>

    suspend fun getUsers(forceUpdate: Boolean = false): List<User>


    suspend fun getUserByEmail(mail: String): User?

    suspend fun getUserByid(id: String): User?

    suspend fun deleteUser(user: User)

    suspend fun saveUserMail(mail: String)

    suspend fun deleteUserMail()

    suspend fun getUserMail(): String?

    suspend fun approveUser(user: User)

    suspend fun createUser(firstName: String,
       lastName: String,
       mail: String,
       password: String,
        approved: Boolean = false
    ): User?


    fun getUserFlow(user: User): Flow<User>

    suspend fun updateToken(user: User, token: String)
}