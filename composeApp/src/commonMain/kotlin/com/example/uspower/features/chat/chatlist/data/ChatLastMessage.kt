package com.example.uspower.features.chat.chatlist.data

import dev.gitlive.firebase.firestore.Timestamp

data class ChatLastMessage(
    val firstName: String,
    val message: String,
    val timestamp: Timestamp
)