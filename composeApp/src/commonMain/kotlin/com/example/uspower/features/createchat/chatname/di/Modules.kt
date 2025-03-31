package com.example.uspower.features.createchat.chatname.di

import com.example.uspower.features.createchat.chatname.ChatNameComponent
import com.example.uspower.features.createchat.chatname.ChatNameComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val chatNameModule = module {
    single <ChatNameComponent.Factory> {
        ChatNameComponentImpl.Factory()
    }.bind<ChatNameComponent.Factory>()
}