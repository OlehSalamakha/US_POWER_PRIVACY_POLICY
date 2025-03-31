package com.example.uspower.features.chat.editchat.addmembers

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.features.chat.editchat.addmembers.addActionComponent.AddActionComponent
import com.example.uspower.features.createchat.addusers.AddUsersToChatComponent
import com.example.uspower.models.Chat
import kotlinx.coroutines.flow.StateFlow

interface AddMembersRootComponent {

    val selectUsersComponent: AddUsersToChatComponent

    val addActionComponent: AddActionComponent

    val showProgress: StateFlow<Boolean>



    fun showProgress(show: Boolean)


    interface Factory {
        fun create(componentContext: ComponentContext, chat: Chat, goBack: () -> Unit): AddMembersRootComponent
    }

}