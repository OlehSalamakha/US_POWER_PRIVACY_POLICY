package com.example.uspower.features.chat.editchat.di


import com.example.uspower.features.chat.editchat.EditChatComponent
import com.example.uspower.features.chat.editchat.EditChatComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val editChatModule = module {
    single<EditChatComponent.Factory> {
        EditChatComponentImpl.Factory(
           get(), get(), get(), get(), get()
        )
    }.bind<EditChatComponent.Factory>()
}