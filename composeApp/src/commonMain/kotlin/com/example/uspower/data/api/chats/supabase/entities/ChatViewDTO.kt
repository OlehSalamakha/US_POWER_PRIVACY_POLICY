package com.example.uspower.data.api.chats.supabase.entities

import com.example.uspower.data.api.messages.toMessage
import com.example.uspower.models.Chat
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChatViewDTO(
    @SerialName("chatid")
    val chatid: String,

    @SerialName("chatname")
    val name: String,

    @SerialName("participants")
    val participants: List<String>,

    @SerialName("owners")
    val owners: List<String>,

    @SerialName("lastmessage")
    val lastMessage: String?,

    @SerialName("message_timestamp")
    val lastMessageTimeStamp: Instant?,

    @SerialName("messagetype")
    val messageType: Int?,

    @SerialName("firstname")
    val senderName: String?,

    @SerialName("global")
    val global: Boolean,

    @SerialName("photourl")
    val photourl: String,
)


fun ChatViewDTO.toChat(unreadCount: Int = 0): Chat {
    return Chat(
        chatId = this.chatid,
        name = this.name,
        photoUrl = this.photourl,
        owners = this.owners.toMutableList(),
        participants = this.participants.toMutableList(),
        global = this.global,
        lastMessage = this.lastMessage,
        lastMessageTimeStamp = if (this.lastMessageTimeStamp != null) Timestamp(this.lastMessageTimeStamp.epochSeconds, 0) else null,
        lastSender = this.senderName,
        unreadCount = unreadCount
    )
}
