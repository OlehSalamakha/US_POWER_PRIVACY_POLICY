package com.example.uspower.features.createchat.addandcreate

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.example.uspower.features.createchat.addusers.AddUsersToChatComponent
import com.example.uspower.features.createchat.create.CreateChatComponent
import kotlinx.coroutines.flow.MutableStateFlow

class AddAndCreateComponentImpl (
    val componentContext: ComponentContext,
    private val addUsersToChatComponentFactory: AddUsersToChatComponent.Factory,
    private val createChatComponentFactory: CreateChatComponent.Factory,
    val chatName: String,
    val goBack: () -> Unit,
): ComponentContext by componentContext, AddAndCreateComponent {
    override val addUsersComponent: AddUsersToChatComponent = addUsersToChatComponentFactory.create(
        childContext("addUsersComponent"),
    )

    override val createChatComponent: CreateChatComponent = createChatComponentFactory.create(
        childContext("createChatComponent"),
        chatName,
        addUsersComponent.selectedUsers,
        goBack,
        ::showProgress
    )
    override val showProgress = MutableStateFlow(false)
    override fun showProgress(show: Boolean) {
        showProgress.value = show
    }

    class Factory(
        private val addUsersToChatComponentFactory: AddUsersToChatComponent.Factory,
        private val createChatComponentFactory: CreateChatComponent.Factory,
    ): AddAndCreateComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            chatName: String,
            goBack: () -> Unit
        ): AddAndCreateComponent {
            return AddAndCreateComponentImpl(componentContext, addUsersToChatComponentFactory, createChatComponentFactory, chatName, goBack)
        }

    }
}