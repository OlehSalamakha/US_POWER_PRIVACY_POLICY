package com.example.uspower.core.messages

import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.models.Message
import com.example.uspower.models.MessageType
import com.example.uspower.models.ReactionEmoji
import com.example.uspower.models.toType
import com.example.uspower.notifications.NotificationSender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageProviderImpl(
    private val loginManager: LoginManager,
    private val usersRepository: UsersRepository,
    private val messageRepository: MessageRepository,
    private val notificationSender: NotificationSender

): MessageProvider {
    override suspend fun getMessagesWithPagination(
        chatId: String,
        message: Message?
    ): List<Message> {
        return messageRepository.getMessagesWithPagination(
            chatId,
            message
        ).map { itemMessage ->
            if (itemMessage.sender != loginManager.user?.uuid) {
                val user = usersRepository.getUserByid(itemMessage.sender)
                itemMessage.senderAvatar = user?.photoUrl
                itemMessage.senderName = user?.firstName + " " + user?.lastName
            }

            itemMessage
        }
    }

    override suspend fun sendMessage(chatId: String, message: Message) {
        messageRepository.sendMessage(chatId, message)
        sendNotification(chatId, message)
    }

    override fun subscribeToMessages(chatId: String): Flow<List<Message>> {
        return messageRepository.subscribeToMessages(chatId).map { itemMessage ->
            itemMessage.map {
                if (it.sender != loginManager.user?.uuid) {
                    val userById = usersRepository.getUserByid(it.sender)
                    it.senderAvatar = userById?.photoUrl
                    it.senderName = userById?.firstName + " " + userById?.lastName
                }
                it.isFromMe = it.sender == loginManager.user?.uuid
                it
            }
        }
    }

    private suspend fun sendNotification(chatId: String, message: Message) {
        val title = "${loginManager.user?.firstName} ${loginManager.user?.lastName}"
        val body = when(message.type.toType()) {
            MessageType.TEXT -> message.content
            MessageType.IMAGE -> "(photo)"
            MessageType.FILE -> "(file)"
        }
        val userId = loginManager.user?.email.toString()

        notificationSender.sendMessageNotification(title, body, chatId, userId)
    }

    override suspend fun subscribeToRemovedMessages(chatId: String): Flow<List<Message>> {
        return messageRepository.subscribeToRemovedMessages(chatId)
    }

    override suspend fun addOrRemoveEmoji(chatId: String, emoji: ReactionEmoji, message: Message) {
        messageRepository.addOrRemoveEmoji(chatId, emoji, message, loginManager.user?.uuid.orEmpty())
    }

    override suspend fun removeMessage(chatId: String, messages: Message) {
        messageRepository.removeMessage(chatId, messages)
    }
}