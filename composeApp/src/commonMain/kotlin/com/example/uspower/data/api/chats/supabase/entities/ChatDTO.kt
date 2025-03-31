package com.example.uspower.data.api.chats.supabase.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChatDTO(

    @SerialName("uuid")
    val uuid: String,

    @SerialName("photourl")
    val photoUrl: String,

    @SerialName("global")
    val global: Boolean,

    @SerialName("chatname")
    val chatName: String
)