package com.example.uspower.core.users


import com.example.uspower.data.api.users.UsersApi
import com.example.uspower.data.datastore.LocalPreferences
import com.example.uspower.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

class UsersRepositoryImpl(
    private val usersApi: UsersApi,
    private val localPreferences: LocalPreferences,
    private val coroutineScope: CoroutineScope,
): UsersRepository {

    private var usersCache: List<User> = emptyList()

    private val _usersFlow: SharedFlow<List<User>> = usersApi.getUsersFlow()
        .flowOn(Dispatchers.IO)
        .onEach {
            usersCache = it
        }
        .shareIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            replay = 1
        )

    override val usersFLow: Flow<List<User>> = _usersFlow


    override suspend fun getUsers(forceUpdate: Boolean): List<User> {
        return if (!forceUpdate && usersCache.isNotEmpty()) {
            usersCache
        } else {
            usersApi.getUsers().also { usersCache = it }
        }
    }


    override suspend fun getUserByEmail(mail: String): User? {
        try {
            return usersCache.firstOrNull { it.email == mail }
                ?:  usersApi.getUserByEmail(mail)
        } catch (exception: Throwable) {
            println("100500 exception is $exception, stacktrace ${exception.printStackTrace()}")
            return null
        }

    }

    override suspend fun getUserByid(id: String): User? {
        try {
            return usersCache.firstOrNull { it.uuid == id }
                ?:  usersApi.getUserById(id)
        } catch (exception: Throwable) {
            println("100500 exception is $exception, stacktrace ${exception.printStackTrace()}")
            return null
        }
    }

    override suspend fun deleteUser(user: User) {
        usersApi.deleteUser(user)
    }

    override suspend fun saveUserMail(mail: String) {
        localPreferences.saveEMailToPreferences(mail)
    }

    override suspend fun deleteUserMail() {
        localPreferences.removeEMailFromPreferences()
    }

    override suspend fun getUserMail(): String? {
        return localPreferences.getEMailFromPreferences()
    }

    override suspend fun approveUser(user: User) {
        usersApi.updateUser(user, mapOf(
            "approved" to true
        ))
    }


    override suspend fun createUser(firstName: String, lastName: String, mail: String, password: String, approved: Boolean): User? {
        return usersApi.createUser(firstName, lastName, mail, password, approved)
    }

    override fun getUserFlow(user: User): Flow<User> {
        return usersApi.getSingleUserFlow(user)
    }

    override suspend fun updateToken(user: User, token: String) {
        usersApi.updateUser(user, mapOf(
            "fcmToken" to token
        ))
    }
}