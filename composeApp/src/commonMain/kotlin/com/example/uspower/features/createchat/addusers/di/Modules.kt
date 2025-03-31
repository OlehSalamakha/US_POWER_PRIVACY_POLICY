package com.example.uspower.features.createchat.addusers.di


import com.example.uspower.features.createchat.addusers.AddUsersToChatComponent
import com.example.uspower.features.createchat.addusers.AddUsersToChatComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val addUsersToChatModule = module {
    single<AddUsersToChatComponent.Factory> {
        AddUsersToChatComponentImpl.Factory(get(), get())
    }.bind<AddUsersToChatComponent.Factory>()
}