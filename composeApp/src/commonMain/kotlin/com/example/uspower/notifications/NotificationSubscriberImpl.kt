package com.example.uspower.notifications


import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class NotificationSubscriberImpl(
    private val httpClient: HttpClient

): NotificationSubscriber {

    init {
        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                println("Push Notification onNewToken: $token")
            }

            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                println("Push Notification notification type message is received: Title: $title and Body: $body")
            }

            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                println("Push Notification payloadData: $data")
            }

            override fun onNotificationClicked(data: PayloadData) {
                super.onNotificationClicked(data)
                println("Notification clicked, Notification payloadData: $data")
            }
        })
    }



    override suspend fun subscribeToTopics(topics: List<String>) {
        topics.forEach {
            NotifierManager.getPushNotifier().subscribeToTopic(it)
        }
    }

    override suspend fun subscribeToTopic(topic: String) {
       subscribeToTopics(listOf(topic))
    }

    override suspend fun unsubscribeFromTopic(fcmToken: String, topic: String) {
        val url = "https://us-central1-us-power-app.cloudfunctions.net/unsubscribeUserFromTopic"

        val data = mapOf(
            "registrationToken" to fcmToken,
            "topicName" to topic,
        )

        val requestBody = mapOf("data" to data)

        val response = httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        println("unsubscribe from topic response is ${response.body<String>()}")
    }

    override suspend  fun unsubscribeFromTopics(fcmToken: String, topics: List<String>) {
        topics.forEach {
            unsubscribeFromTopic(fcmToken, it)
        }
    }
}