package com.example.uspower.features.createchat.create

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.models.User
import kotlinx.coroutines.flow.StateFlow

interface CreateChatComponent {

    val enabledCreationButton: StateFlow<Boolean>
    fun createChat()

    interface Factory {
        fun create(componentContext: ComponentContext,
                   chatName: String,
                   usersFlow: StateFlow<Set<User>>,
                   goBack: () -> Unit,
                   showProgress: (Boolean) -> Unit
        ): CreateChatComponent
    }
}