package com.example.uspower.core.messages

import com.example.uspower.models.Message
import com.example.uspower.models.ReactionEmoji
import com.example.uspower.models.User
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessagesWithPagination(chatId: String, message: Message?): List<Message>
    suspend fun sendMessage(chatId: String, message: Message)
    fun subscribeToMessages(chatId: String): Flow<List<Message>>
   suspend fun subscribeToRemovedMessages(chatId: String): Flow<List<Message>>
    suspend fun addOrRemoveEmoji(chatId: String, emoji: ReactionEmoji, message: Message, sender: String)
    suspend fun removeMessage(chatId: String, messages: Message)
}