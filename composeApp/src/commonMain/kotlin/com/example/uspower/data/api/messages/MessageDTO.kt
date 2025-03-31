package com.example.uspower.data.api.messages

import com.example.uspower.models.Message
import com.example.uspower.models.Reaction
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDTO(
    @SerialName("uuid")
    val uuid: String,

    @SerialName("timestamp")
    val timestamp: Instant,


    @SerialName("content")
    val content: String,

    @SerialName("type")
    val type: Int,

    @SerialName("senderid")
    val senderId: String,

    @SerialName("chatid")
    val chatid: String,

    @SerialName("reactions")
    val reactions: List<ReactionDTO>?,


    @SerialName("filename")
    val fileName: String = "",

    @SerialName("is_first_message_of_the_day")
    val isFirstMessageOfTheDay: Boolean
)

@Serializable
data class ReactionDTO(
    @SerialName("sender")
    val sender: String,
    @SerialName("reaction")
    val reaction: String
)


fun MessageDTO.toMessage(): Message {
    return Message(
        messageId = this.uuid,
        sender = this.senderId,
        content = this.content,
        timestamp = Timestamp(this.timestamp.epochSeconds, 0),
        type = this.type,
        fileName = this.fileName,
        reactions = this.reactions?.map { it.toReaction() }?.toMutableList(),
        documentSnapshot = null,
        //TODO
        isFromMe = false,
        senderAvatar = null,
        senderName = "",
        isFirstMessageOfTheDay = this.isFirstMessageOfTheDay,
        chatId = this.chatid
    )
}



fun ReactionDTO.toReaction(): Reaction {
    return Reaction(this.sender, this.reaction)
}



