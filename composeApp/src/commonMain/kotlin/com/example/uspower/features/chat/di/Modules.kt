package com.example.uspower.features.chat.di


import com.example.uspower.features.chat.chatlist.ChatListComponent
import com.example.uspower.features.chat.chatlist.ChatListComponentImpl
import com.example.uspower.features.chat.chatlist.data.ChatListProvider
import com.example.uspower.features.chat.chatlist.data.ChatListProviderImpl
import com.example.uspower.features.chat.editchat.UpdateChatImageUseCase
import org.koin.dsl.bind
import org.koin.dsl.module


val chatModule = module {

    single { UpdateChatImageUseCase(get(), get()) }
    single<ChatListComponent.Factory> {
        ChatListComponentImpl.Factory(
            chatListProvider = get(),
        )
    }.bind<ChatListComponent.Factory>()


    single { ChatListProviderImpl(
        get(), get(), get(), get()
    ) }.bind<ChatListProvider>()
}

