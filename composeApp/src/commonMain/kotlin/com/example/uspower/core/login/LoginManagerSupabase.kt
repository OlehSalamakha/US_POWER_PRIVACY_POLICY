package com.example.uspower.core.login

import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.features.profileflow.profile.DeleteProfileUseCase
import com.example.uspower.models.User
import kotlinx.coroutines.flow.Flow

class LoginManagerSupabase(
    private val usersRepository: UsersRepository,
    private val deleteProfileUseCase: DeleteProfileUseCase,
    private val chatsRepository: ChatRepository,
): LoginManager {

    override var user: User? = null

    /*
        TODO change to user exists
     */
    override suspend fun login(mail: String): User? {
        user = usersRepository.getUserByEmail(mail)
        return user
    }

    override suspend fun signIn(mail: String, password: String): User?  {
        val user = usersRepository.getUserByEmail(mail)
        this.user = user
        this.user?.let {
            val dbPassword = user?.password ?: ""
            if (password == decode(dbPassword)) {

                return this.user
            }
        }

        this.user = null
        return this.user
    }

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        mail: String,
        confirmationMail: String,
        password: String
    ): User? {
        val user = usersRepository.createUser(
            firstName,
            lastName,
            mail,
            encode(password)
        )

        this.user = user
        return user
    }

    override suspend fun saveUserToLocalPreferences(mail: String) {
        usersRepository.saveUserMail(mail)
    }

    override suspend fun readUserFromLocalPreferences(): String {
        return usersRepository.getUserMail() ?: ""
    }

    override suspend fun updateToken(newToken: String) {
        /*
            TODO UPDATE TOKEN??

         */
    }

    override fun getCurrentUserFlow(): Flow<User> {
        return usersRepository.getUserFlow(user!!)
    }

    override suspend fun logout() {
        usersRepository.deleteUserMail()
        chatsRepository.removeCachedChats()
        user = null
    }

    override suspend fun deleteProfile() {
        user?.let {
            deleteProfileUseCase.deleteAccount(it)
        }
        user = null

    }

}


fun decode(encodedPassword: String): String {
    var decodedPassword = ""
    for (char in encodedPassword) {
        val decodedChar = char - 3
        decodedPassword += decodedChar
    }
    return decodedPassword
}


fun encode(password: String): String {
    var encodedPassword = ""
    for (char in password) {
        val encodedChar = char + 3
        encodedPassword += encodedChar
    }
    return encodedPassword
}