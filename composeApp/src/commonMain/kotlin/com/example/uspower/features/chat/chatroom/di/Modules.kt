package com.example.uspower.features.chat.chatroom.di

import com.example.uspower.features.chat.chatroom.ChatRoomComponent
import com.example.uspower.features.chat.chatroom.ChatRoomComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val chatRoomModule = module {
    single<ChatRoomComponent.Factory> {
        ChatRoomComponentImpl.Factory(
            messageProvider = get(),
            loginManager = get(),
            usersRepository = get(),
            chatRepository = get(),
            filesApi = get(),
            externalCoroutineScope = get()
        )
    }.bind<ChatRoomComponent.Factory>()
}
