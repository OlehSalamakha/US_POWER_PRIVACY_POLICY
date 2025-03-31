package com.example.uspower.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.uspower.data.api.files.PlatformSpecificUploader
import com.example.uspower.data.datastore.DATA_STORE_FILE_NAME
import com.example.uspower.data.datastore.createDataStore
import com.example.uspower.files.IosFileUploader
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual val platformModule: Module = module {
    single {
        createDataStoreee()
    }

    single {
        IosFileUploader(get())
    }.bind<PlatformSpecificUploader>()
}

@OptIn(ExperimentalForeignApi::class)
fun createDataStoreee(): DataStore<Preferences> {
    return createDataStore {
        var directory = NSFileManager.defaultManager().URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        directory!!.path!! + "/$DATA_STORE_FILE_NAME"
    }
}
