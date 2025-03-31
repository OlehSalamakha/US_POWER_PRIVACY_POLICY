package com.example.uspower.features.createchat.di


import com.example.uspower.features.createchat.NewChatFLowComponentImpl
import com.example.uspower.features.createchat.NewChatFlowComponent
import org.koin.dsl.bind
import org.koin.dsl.module


val addNewChatModule = module {
    single<NewChatFlowComponent.Factory> {
        NewChatFLowComponentImpl.Factory(
            chatNameComponentFactory = get(),
            addUsersToChatComponentFactory = get()
        )
    }.bind<NewChatFlowComponent.Factory>()

}