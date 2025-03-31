package com.example.uspower.notifications


interface NotificationSubscriber {
    suspend fun subscribeToTopics(topics: List<String>)
    suspend fun subscribeToTopic(topic: String)

    suspend fun unsubscribeFromTopic(fcmToken: String, topic: String)
    suspend fun unsubscribeFromTopics(fcmToken: String, topics: List<String>)
}