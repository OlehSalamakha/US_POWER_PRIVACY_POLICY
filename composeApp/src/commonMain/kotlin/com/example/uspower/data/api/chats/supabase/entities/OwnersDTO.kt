package com.example.uspower.data.api.chats.supabase.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OwnersDTO(
    @SerialName("chatid")
    val chatid: String,

    @SerialName("userid")
    val userid: String,
)