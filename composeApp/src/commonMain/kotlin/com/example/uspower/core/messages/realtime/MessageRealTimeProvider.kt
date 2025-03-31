package com.example.uspower.core.messages.realtime

import com.example.uspower.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRealTimeProvider {
    fun subscribeToChat(chatId: String): Flow<MessageRealTimeEvent>
}


sealed class MessageRealTimeEvent(val message: Message) {
    class Insert(message: Message): MessageRealTimeEvent(message)
    class Delete(message: Message): MessageRealTimeEvent(message)
    class Update(message: Message): MessageRealTimeEvent(message)

}