package com.example.uspower.core.chat

import com.example.uspower.core.LoadState
import com.example.uspower.data.api.chats.supabase.entities.UnreadCountDto
import com.example.uspower.models.Chat
import com.example.uspower.models.User
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun getChats(user: User): List<Chat>
    fun subscribeToChats(user: User): Flow<LoadState<List<Chat>>>

    fun subscribeToChat(chat: Chat): Flow<LoadState<Chat>>

    suspend fun createChat(
        user: User,
        chatName: String,
        chatMembers: List<User>
    )

    suspend fun deleteChat(chat: Chat)

    suspend fun addMembersToChat(chat: Chat, newMembers: List<User>)

    suspend fun removeUserFromChat(chat: Chat, userMail: User)

    suspend fun renameChat(chat: Chat, newName: String)

    suspend fun removeCachedChats()

    suspend fun updateLastReadAt(user: User, chat: Chat, timestamp: Timestamp)

    suspend fun getCountOfUnreadMessages(user: User): List<UnreadCountDto>



    suspend fun updateChatPhoto(chat: Chat, photoUrl: String)
}