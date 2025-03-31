package com.example.uspower.models

import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.Timestamp

data class Message(
    val messageId: String,
    val sender: String,
    var content: String,
    var chatId: String,
    val timestamp: Timestamp,
    var type: Int, // 1 - Text, // 2 - Photo, // 3 - File
    val fileName: String,
    var reactions: MutableList<Reaction>?,
    var isFirstMessageOfTheDay: Boolean = false,
    var documentSnapshot: DocumentSnapshot? = null,
    var isFromMe: Boolean = false,
    var senderAvatar: String? = null,
    var senderName: String? = null,
    var date: String? = null,
)

data class Reaction(
    val sender: String,
    var content: String
)

fun Int.toType(): MessageType {
    return when (this) {
        1 -> MessageType.TEXT
        2 -> MessageType.IMAGE
        3 -> MessageType.FILE
        else -> MessageType.TEXT
    }
}

const val LOADING_IMAGE= 4
enum class MessageType(val code: Int) {
    TEXT(1), IMAGE(2), FILE(3)
}

enum class ReactionEmoji(val code: String) {
    HURT("❤\uFE0F"), LIKE("\uD83D\uDC4D"), WOW("\uD83D\uDE2E"), LAUGHING("\uD83D\uDE02"), GREAT("\uD83D\uDC4F"), CUTE("☺\uFE0F"), SMILE("\uD83D\uDE09"), UKNOWN("")
}

fun Reaction.toReactionEmoji(): ReactionEmoji {

    val result =  ReactionEmoji.entries.find {
        println("100500, code  emoji is ${it.code}, emoji congtent is ${this.content}, ${it.code == this.content}")
        it.code == this.content
    } ?: ReactionEmoji.UKNOWN

    println("100500 result is $result")
    return result
}

fun ReactionEmoji.toReaction(sender: String): Reaction {
    return Reaction(sender = sender, content = this.code)
}

