package com.example.uspower.features.chat.editchat.addmembers

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.example.uspower.features.chat.editchat.addmembers.addActionComponent.AddActionComponent
import com.example.uspower.features.createchat.addusers.AddUsersToChatComponent
import com.example.uspower.models.Chat
import kotlinx.coroutines.flow.MutableStateFlow

class AddMembersRootComponentImpl(
    componentContext: ComponentContext,
    private val addActionComponentFactory: AddActionComponent.Factory,
    private val selectUsersComponentFactory: AddUsersToChatComponent.Factory,
    val chat: Chat,
    val goBack: () -> Unit
): ComponentContext by componentContext, AddMembersRootComponent {
    override val selectUsersComponent: AddUsersToChatComponent =
        selectUsersComponentFactory.create(
            childContext("selectUsersToAdd"),
            userFilter = {user -> chat.participants?.contains(user.email) != true }
        )
    override val addActionComponent: AddActionComponent =
        addActionComponentFactory.create(
            childContext("addActionComponent"),
            chat,
            selectUsersComponent.selectedUsers,
            goBack,
            ::showProgress
        )
    override val showProgress = MutableStateFlow(false)
    override fun showProgress(show: Boolean) {
        showProgress.value = show
    }


    class Factory(
        val addActionComponentFactory: AddActionComponent.Factory,
        val selectUsersComponentFactory: AddUsersToChatComponent.Factory,
    ): AddMembersRootComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            chat: Chat,
            goBack: () -> Unit
        ): AddMembersRootComponent {
            return AddMembersRootComponentImpl(
                componentContext,
                addActionComponentFactory,
                selectUsersComponentFactory,
                chat,
                goBack
            )
        }

    }


}