package com.example.uspower.features.createchat

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.push
import com.example.uspower.base.FlowComponentImpl
import com.example.uspower.features.createchat.addandcreate.AddAndCreateComponent

import com.example.uspower.features.createchat.chatname.ChatNameComponent

import kotlinx.serialization.KSerializer

class NewChatFLowComponentImpl(
    componentContext: ComponentContext,
    private val chatNameComponentFactory: ChatNameComponent.Factory,
    private val addUsersToChatComponentFactory: AddAndCreateComponent.Factory,
    private val goBack: () -> Unit
): ComponentContext by componentContext, NewChatFlowComponent,
    FlowComponentImpl<NewChatFlowComponent.Child, NewChatFLowComponentImpl.Config>() {

    override val initialConfiguration: Config = Config.ChatNameConfig
    override val configSerializer: KSerializer<Config> = Config.serializer()
    override val handleBack: Boolean = true

    fun goToUserSelection(chatName: String) {
        nav.push(Config.AddAndCreate(chatName))
    }

    override fun child(
        config: Config,
        componentContext: ComponentContext
    ): NewChatFlowComponent.Child {
        return when(config) {
            is Config.AddAndCreate -> NewChatFlowComponent.Child.AddAndCreate(
                addUsersToChatComponentFactory.create(componentContext, config.chatName, goBack)
            )
            Config.ChatNameConfig -> NewChatFlowComponent.Child.ChatName(
                chatNameComponentFactory.create(componentContext, ::goToUserSelection, goBack)
            )
        }
    }


    @kotlinx.serialization.Serializable
     sealed interface Config {
        @kotlinx.serialization.Serializable
        data object ChatNameConfig : Config

        @kotlinx.serialization.Serializable
        data class AddAndCreate(val chatName: String) : Config

    }

    class Factory(
        private val chatNameComponentFactory: ChatNameComponent.Factory,
        private val addUsersToChatComponentFactory: AddAndCreateComponent.Factory
    ): NewChatFlowComponent.Factory {
        override fun create(componentContext: ComponentContext, goBack: () -> Unit): NewChatFlowComponent {
            return NewChatFLowComponentImpl(componentContext, chatNameComponentFactory, addUsersToChatComponentFactory, goBack)
        }

    }

}