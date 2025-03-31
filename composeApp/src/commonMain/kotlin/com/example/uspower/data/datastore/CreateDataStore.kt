package com.example.uspower.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath


fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return androidx.datastore.preferences.core.PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath()}
    )
}

internal const val DATA_STORE_FILE_NAME: String = "prefs.preferences_pb"