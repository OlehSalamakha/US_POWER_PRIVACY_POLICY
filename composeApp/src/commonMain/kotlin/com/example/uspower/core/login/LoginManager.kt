package com.example.uspower.core.login

import com.example.uspower.models.User
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.Flow

interface LoginManager {
    var user: User?


    suspend fun login(mail: String): User?

    suspend fun signIn(mail: String, password: String): User?

    suspend fun signUp(firstName: String, lastName: String, mail: String, confirmationMail: String, password: String): User?

    suspend fun saveUserToLocalPreferences(mail: String)

    suspend fun readUserFromLocalPreferences(): String

    suspend fun updateToken(newToken: String)

    fun getCurrentUserFlow(): Flow<User>

    suspend fun logout()

    suspend fun deleteProfile()
}