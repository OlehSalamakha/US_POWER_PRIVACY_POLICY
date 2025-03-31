package com.example.uspower.features.profileflow.profile

import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.models.User
import kotlinx.coroutines.flow.map

class DeleteProfileUseCase(
    val usersRepository: UsersRepository,
    val chatRepository: ChatRepository
) {

    suspend fun deleteAccount(user: User) {
        val chats = chatRepository.getChats(user)

        chats.forEach {
            chatRepository.removeUserFromChat(it, user)
        }

        usersRepository.deleteUser(user)
        usersRepository.deleteUserMail()
    }
}