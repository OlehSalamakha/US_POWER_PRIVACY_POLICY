package com.example.uspower.data.api.chats.supabase.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnreadCountDto(
    @SerialName("chatid")
    val chatId: String,

    @SerialName("unread_count")
    val unreadCount: Int
)
//
//[ {
//    "chatid" : "03543634-d09f-4ad9-8480-99d7251a8cae",
//    "unread_count" : 5
//}, {
//    "chatid" : "3a9e5b47-8048-4061-a836-ea3b384ead0d",
//    "unread_count" : 7
//} ]