package com.example.uspower.features.createchat.chatname

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.features.mainflow.MainFlowComponent
import com.example.uspower.models.Chat
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow

class ChatNameComponentImpl(
    componentContext: ComponentContext,
    private val goToUserSelection: (String) -> Unit,
    private val goBack: () -> Unit,
): ComponentContext by componentContext, ChatNameComponent {
    override val chatName = MutableStateFlow("")


    override fun onNameChanged(name: String) {
        chatName.value = name
    }

    override fun onNextClicked() {
        goToUserSelection(chatName.value)
    }

    override fun goBackClicked() {
        goBack()
    }

    class Factory: ChatNameComponent.Factory {
        override fun create(componentContext: ComponentContext, goToUserSelection: (String) -> Unit, goBack: () -> Unit): ChatNameComponent {
            return ChatNameComponentImpl(componentContext, goToUserSelection, goBack)
        }

    }


}