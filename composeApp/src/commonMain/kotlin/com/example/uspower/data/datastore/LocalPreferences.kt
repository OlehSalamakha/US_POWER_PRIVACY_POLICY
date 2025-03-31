package com.example.uspower.data.datastore


interface LocalPreferences {

    suspend fun getEMailFromPreferences(): String?

    suspend fun saveEMailToPreferences(mail: String)

    suspend fun removeEMailFromPreferences()

    suspend fun saveChatsToList(chats: String)

    suspend fun getChats(): String?

    suspend fun removeChatsFromPreferences()
}