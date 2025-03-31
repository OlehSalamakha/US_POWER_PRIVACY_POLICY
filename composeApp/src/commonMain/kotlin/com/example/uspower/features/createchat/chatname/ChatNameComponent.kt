package com.example.uspower.features.createchat.chatname

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface ChatNameComponent {

    val chatName: StateFlow<String>

    fun onNameChanged(name: String)

    fun onNextClicked()

    fun goBackClicked()

    interface Factory {
        fun create(componentContext: ComponentContext, goToUsersSelection: (String) -> Unit, goBack: () -> Unit): ChatNameComponent
    }
}