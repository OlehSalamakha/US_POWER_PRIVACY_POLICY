package com.example.uspower.data.api.users

import com.example.uspower.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

interface UsersApi {

    suspend fun getUsers(): List<User>

    fun getUsersFlow(): Flow<List<User>>

    fun getSingleUserFlow(user: User): Flow<User>

    suspend fun getUserByEmail(mail: String): User?

    suspend fun getUserById(id: String): User?

    suspend fun createUser(firstName: String, lastName: String, mail: String, password: String, approved: Boolean): User?

    suspend fun deleteUser(user: User)

    suspend fun updateUser(
        user: User,
        map: Map<String, Any>
    )

}