package com.example.uspower.di

import com.example.uspower.createDataStore
import com.example.uspower.data.api.files.PlatformSpecificUploader
import com.example.uspower.files.AndroidFileUploader
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { createDataStore(get()) }

    single { AndroidFileUploader(get()) }.bind<PlatformSpecificUploader>()
}