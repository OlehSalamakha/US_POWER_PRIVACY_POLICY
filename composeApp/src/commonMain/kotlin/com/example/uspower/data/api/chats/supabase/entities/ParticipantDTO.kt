package com.example.uspower.data.api.chats.supabase.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParticipantDTO(
    @SerialName("chatid")
    val chatid: String,

    @SerialName("userid")
    val userid: String,

    @SerialName("last_read_at")
    val lastRead: Instant? = null
)