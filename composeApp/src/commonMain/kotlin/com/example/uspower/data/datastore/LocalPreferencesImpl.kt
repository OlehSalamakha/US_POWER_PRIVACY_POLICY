package com.example.uspower.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalPreferencesImpl(private val dataStore: DataStore<Preferences>): LocalPreferences {

    private val userMailKey = stringPreferencesKey("USER_MAIL")

    private val chatsKey = stringPreferencesKey("CHATS")


    override suspend fun getEMailFromPreferences(): String? {
        return dataStore.data.map { preferences ->
            preferences[userMailKey]
        }.first()
    }

    override suspend fun saveEMailToPreferences(mail: String) {
        dataStore.edit { preferences ->
            preferences[userMailKey] = mail
        }
    }

    override suspend fun removeEMailFromPreferences() {
        dataStore.edit { prefereces ->
            prefereces.remove(userMailKey)
        }
    }

    override suspend fun saveChatsToList(chats: String) {
        dataStore.edit { preferences ->
            preferences[chatsKey] = chats
        }
    }

    override suspend fun getChats(): String? {
        return dataStore.data.map { preferences ->
            preferences[chatsKey]
        }.first()
    }

    override suspend fun removeChatsFromPreferences() {
        dataStore.edit { prefereces ->
            prefereces.remove(chatsKey)
        }
    }
}