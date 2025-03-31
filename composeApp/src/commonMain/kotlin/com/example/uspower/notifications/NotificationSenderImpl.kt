package com.example.uspower.notifications

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NotificationSenderImpl(
    private val httpClient: HttpClient
): NotificationSender {

    private val url = "https://us-central1-us-power-app.cloudfunctions.net/sendMessageToTopicWithUserID"

    override suspend fun sendMessageNotification(
        title: String,
        body: String,
        topic: String,
        userId: String
    ) {

        val data = mapOf(
            "title" to title,
            "body" to body,
            "topic" to topic,
            "userID" to userId
        )

        val requestBody = mapOf("data" to data)

        httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
    }
}