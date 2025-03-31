package com.example.uspower.di

import com.example.uspower.files.DesktopFileUploader
import com.example.uspower.data.api.files.PlatformSpecificUploader
import com.example.uspower.data.datastore.DATA_STORE_FILE_NAME
import com.example.uspower.data.datastore.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single {
        println("Create data store desktop")
        createDataStore(
            {"./$DATA_STORE_FILE_NAME"}
        ) }


    single {
        DesktopFileUploader(get())
    }.bind<PlatformSpecificUploader>()


}