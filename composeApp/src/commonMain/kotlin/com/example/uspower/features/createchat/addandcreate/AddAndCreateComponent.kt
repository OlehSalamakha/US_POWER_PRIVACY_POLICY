package com.example.uspower.features.createchat.addandcreate

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.features.createchat.addusers.AddUsersToChatComponent
import com.example.uspower.features.createchat.create.CreateChatComponent
import kotlinx.coroutines.flow.StateFlow

interface AddAndCreateComponent {

    val addUsersComponent: AddUsersToChatComponent

    val createChatComponent: CreateChatComponent

    val showProgress: StateFlow<Boolean>

    fun showProgress(show: Boolean)


    interface Factory {
        fun create(componentContext: ComponentContext, chatName: String, goBack: () -> Unit): AddAndCreateComponent
    }

}