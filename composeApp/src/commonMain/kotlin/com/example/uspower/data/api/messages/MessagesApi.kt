package com.example.uspower.data.api.messages

import com.example.uspower.models.Message
import com.example.uspower.models.ReactionEmoji
import kotlinx.coroutines.flow.Flow

interface MessagesApi {
    suspend fun getMessagesWithPagination(chatId: String, message: Message?): List<Message>

    suspend fun sendMessage(chatId: String, message: Message)
    suspend fun addOrRemoveEmoji(chatId: String, emoji: ReactionEmoji, message: Message, sender: String)
    suspend fun removeMessage(chatId: String, messages: Message)
}