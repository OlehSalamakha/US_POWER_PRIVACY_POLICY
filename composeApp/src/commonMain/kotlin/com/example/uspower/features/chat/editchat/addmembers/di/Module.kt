package com.example.uspower.features.chat.editchat.addmembers.di

import com.example.uspower.features.chat.editchat.addmembers.AddMembersRootComponent
import com.example.uspower.features.chat.editchat.addmembers.AddMembersRootComponentImpl
import com.example.uspower.features.chat.editchat.addmembers.addActionComponent.AddActionComponent
import com.example.uspower.features.chat.editchat.addmembers.addActionComponent.AddActionComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val addMembersToChatModule = module {
    single<AddMembersRootComponent.Factory> {
        AddMembersRootComponentImpl.Factory(get(), get())
    }.bind<AddMembersRootComponent.Factory>()


    single<AddActionComponent.Factory> {
        AddActionComponentImpl.Factory(get())
    }.bind<AddActionComponent.Factory>()
}