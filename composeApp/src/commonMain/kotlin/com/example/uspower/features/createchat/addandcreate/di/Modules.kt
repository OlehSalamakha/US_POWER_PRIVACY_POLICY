package com.example.uspower.features.createchat.addandcreate.di

import com.example.uspower.features.createchat.addandcreate.AddAndCreateComponent
import com.example.uspower.features.createchat.addandcreate.AddAndCreateComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val addAndCreateChatModule = module {
    single<AddAndCreateComponent.Factory> {
        AddAndCreateComponentImpl.Factory(get(), get())
    }.bind<AddAndCreateComponent.Factory>()
}