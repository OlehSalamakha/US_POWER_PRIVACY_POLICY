package com.example.uspower.notifications

interface NotificationSender {
    suspend fun sendMessageNotification(
        title: String,
        body: String,
        topic: String,
        userId: String
    )
}