package com.example.uspower.features.createchat.create.di


import com.example.uspower.features.createchat.addusers.AddUsersToChatComponent
import com.example.uspower.features.createchat.addusers.AddUsersToChatComponentImpl
import com.example.uspower.features.createchat.create.CreateChatComponent
import com.example.uspower.features.createchat.create.CreateChatComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val createChatComponentModule = module {
    single<CreateChatComponent.Factory> {
        CreateChatComponentImpl.Factory(get(), get())
    }.bind<CreateChatComponent.Factory>()
}