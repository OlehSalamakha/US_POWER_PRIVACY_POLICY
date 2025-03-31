package com.example.uspower.core.messages

import com.example.uspower.Log
import com.example.uspower.core.messages.realtime.MessageRealTimeEvent
import com.example.uspower.core.messages.realtime.MessageRealTimeProviderImpl
import com.example.uspower.data.api.messages.MessagesApi
import com.example.uspower.models.Message
import com.example.uspower.models.ReactionEmoji
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val messagesApi: MessagesApi,
    private val messagesRealTimeProviderImpl: MessageRealTimeProviderImpl,
    private val messageCache: MessageCache,
) : MessageRepository {


    override suspend fun getMessagesWithPagination(chatId: String, message: Message?): List<Message> {

        if (message == null) {
            val cached = messageCache.getByKey(chatId)
            Log("MessageRepositoryImpl", "Cached size is ${cached?.values?.size}")
            if (!cached.isNullOrEmpty() && cached.entries.size >= 20) {
                Log("MessageRepositoryImpl", "Get messages from cache")
                return cached.map { it.value }.sortedByDescending { it.timestamp.seconds }
            } else {
                Log("MessageRepositoryImpl", "put messages to cache")
                val messages = messagesApi.getMessagesWithPagination(chatId, message)
                messageCache.addOrUpdate(mapOf(chatId to messages.associateBy({it.messageId}, {it})))
                return messages
            }
        }
        Log("MessageRepositoryImpl", "Get messages from back")
        return messagesApi.getMessagesWithPagination(chatId, message)
    }

    override suspend fun sendMessage(chatId: String, message: Message) {
        return messagesApi.sendMessage(chatId, message)
    }

    override fun subscribeToMessages(chatId: String): Flow<List<Message>> {
        return messagesRealTimeProviderImpl.subscribeToChat(chatId).filter {
            it is MessageRealTimeEvent.Insert || it is MessageRealTimeEvent.Update
        }.map {
            listOf(it.message)
        }

    }

    override suspend fun subscribeToRemovedMessages(chatId: String): Flow<List<Message>> {
        return messagesRealTimeProviderImpl.subscribeToChat(chatId).filter {
            it is MessageRealTimeEvent.Delete
        }.map {
            listOf(it.message)
        }
    }

    override suspend fun addOrRemoveEmoji(chatId: String, emoji: ReactionEmoji, message: Message, sender: String) {
        messagesApi.addOrRemoveEmoji(chatId, emoji, message, sender)
    }

    override suspend fun removeMessage(chatId: String, messages: Message) {
        messagesApi.removeMessage(chatId, messages)
    }

}