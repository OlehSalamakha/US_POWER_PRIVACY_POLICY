package com.example.uspower.features.chat.chatlist.data

import com.example.uspower.core.LoadState
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.messages.MessageRepository
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.data.api.chats.supabase.entities.UnreadCountDto
import com.example.uspower.models.Chat
import com.example.uspower.wrapToLoadState
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

interface ChatListProvider {
    fun subscribeToChat(): Flow<LoadState<List<Chat>>>


    suspend fun getCountOfUnread(): List<UnreadCountDto>
}



class ChatListProviderImpl(
    private val chatRepository: ChatRepository,
    private val usersRepository: UsersRepository,
    private val messageRepository: MessageRepository,
    private val loginManager: LoginManager,
): ChatListProvider {

    override fun subscribeToChat(): Flow<LoadState<List<Chat>>> {
        return chatRepository.subscribeToChats(loginManager.user!!)
    }

    override suspend fun getCountOfUnread(): List<UnreadCountDto> {
       return chatRepository.getCountOfUnreadMessages(loginManager.user!!)
    }


}