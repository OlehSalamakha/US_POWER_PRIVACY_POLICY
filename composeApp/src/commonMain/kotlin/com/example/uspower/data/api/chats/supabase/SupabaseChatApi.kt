package com.example.uspower.data.api.chats.supabase

import com.example.uspower.data.api.chats.supabase.entities.ChatDTO
import com.example.uspower.data.api.chats.supabase.entities.ChatViewDTO
import com.example.uspower.data.api.chats.supabase.entities.UnreadCountDto
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface SupabaseChatApi {
    suspend fun createChat(
        uuid: String,
        name: String,
        photoUrl: String = "",
        global: Boolean = false
    ): ChatDTO


    suspend fun setOwnerToChat(userid: String, chatId: String)

    suspend fun addMembersToChat(chatId: String, newMembers: List<String>)

    suspend fun changeChatName(chatID: String, newName: String)

    suspend fun removeMembersFromChat(chatId: String, memberId: String)

    suspend fun removeChat(chatId: String)

    suspend fun getChats(mail: String):List<ChatViewDTO>

    fun getChatFlow(chatId: String): Flow<ChatViewDTO>

    fun getChatsFlow(mail: String): Flow<List<ChatViewDTO>>

    suspend fun updateLastReadAt(chatId: String, userId: String, timestamp: Instant)

    suspend fun getCountOfUnreadMessages(chatId: String, userid: String): Int

    suspend fun getCountOfUnreadMessagesForAllChats(userId: String): List<UnreadCountDto>



    suspend fun updateChatPhoto(chatId: String, photoUrl: String)
}