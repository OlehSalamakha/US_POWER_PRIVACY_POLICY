package com.example.uspower.features.createchat

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.base.FlowComponent
import com.example.uspower.features.createchat.addandcreate.AddAndCreateComponent
import com.example.uspower.features.createchat.chatname.ChatNameComponent

interface NewChatFlowComponent: FlowComponent<NewChatFlowComponent.Child> {

    sealed interface Child {
        class ChatName(val component: ChatNameComponent): Child
        class AddAndCreate(val component: AddAndCreateComponent): Child
    }

    interface Factory {
        fun create(componentContext: ComponentContext, goBack: () -> Unit): NewChatFlowComponent
    }
}