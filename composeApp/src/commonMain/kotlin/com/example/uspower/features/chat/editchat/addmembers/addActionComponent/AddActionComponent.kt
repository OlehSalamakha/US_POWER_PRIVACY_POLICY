package com.example.uspower.features.chat.editchat.addmembers.addActionComponent

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.models.Chat
import com.example.uspower.models.User
import kotlinx.coroutines.flow.StateFlow

interface AddActionComponent {

    val enableAddButton: StateFlow<Boolean>

    fun addMembers()

    interface Factory {
        fun create(componentContext: ComponentContext,
                   chat: Chat,
                   selectedUsers: StateFlow<Set<User>>,
                   goBack: () -> Unit,
                   showProgress: (Boolean) -> Unit
        ): AddActionComponent
    }
}