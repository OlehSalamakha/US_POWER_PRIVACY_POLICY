package com.example.uspower.features.tabflow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.bringToFront
import com.example.uspower.base.FlowComponentImpl
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.messages.realtime.MessageRealTimeEvent
import com.example.uspower.core.messages.realtime.MessageRealTimeProvider
import com.example.uspower.core.messages.realtime.MessageRealTimeProviderImpl
import com.example.uspower.features.chat.chatlist.ChatListComponent
import com.example.uspower.features.profileflow.profile.ProfileComponent
import com.example.uspower.features.userlist.UsersListComponent
import com.example.uspower.models.Chat
import com.example.uspower.models.User
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class TabFlowComponentImpl(
    componentContext: ComponentContext,
    private val chatFlowComponentFactory: ChatListComponent.Factory,
    private val profileComponentFactory: ProfileComponent.Factory,
    private val usersListComponentFactory: UsersListComponent.Factory,
    private val messageRealtime: MessageRealTimeProvider,
    private val onChatSelected: (chat: Chat) -> Unit,
    private val onUserSelected: (user: User) -> Unit,
    private val onLogout: () -> Unit,
    private val createNewChat: () -> Unit,
    private val onEditProfile: () -> Unit,
    private val createProfile: () -> Unit
): TabFlowComponent, ComponentContext by componentContext,
    FlowComponentImpl<TabFlowComponent.Child, TabFlowComponentImpl.Config>() {


        private val coroutinoScope = componentCoroutineScope()
    override val initialConfiguration: Config = Config.Chat
    override val configSerializer = Config.serializer()
    override val handleBack: Boolean = false
    private  lateinit var messageFlow: Flow<MessageRealTimeEvent>

    init {
        coroutinoScope.launch {
            println("100800 collect latest tab flow")
            messageFlow = messageRealtime.subscribeToChat("")
            messageFlow.collectLatest { data ->
                println("100800 TabFlowComponentImpl data is $data")

            }
        }
    }


    override fun child(
        config: Config,
        componentContext: ComponentContext
    ): TabFlowComponent.Child {
        return when(config) {
            Config.Chat -> TabFlowComponent.Child.Chat(
                chatFlowComponentFactory.create(componentContext,  onChatSelected, createNewChat)
            )

            Config.Profile -> TabFlowComponent.Child.Profile(
                profileComponentFactory.create(componentContext, onLogout, onEditProfile, createProfile)
            )
            Config.Users -> TabFlowComponent.Child.Users(
                usersListComponentFactory.create(componentContext, onUserSelected)
            )
        }
    }

    override fun onChatTabClicked() {
        nav.bringToFront(Config.Chat)
    }

    override fun onUsersTabClicked() {
        nav.bringToFront(Config.Users)
    }

    override fun onProfileTabClicked() {
        nav.bringToFront(Config.Profile)
    }

    override fun onEditProfileClicked() {
        onEditProfile()
    }

    override fun onLogout() {
        onLogout.invoke()
    }

    @Serializable
    sealed interface Config {
        @Serializable
        data object Chat : Config

        @Serializable
        data object Users : Config

        @Serializable
        data object Profile : Config
    }

    class Factory(
        private val chatFlowComponentFactory: ChatListComponent.Factory,
        private val profileComponentFactory: ProfileComponent.Factory,
        private val usersListComponentFactory: UsersListComponent.Factory,
        private val messageRealtimeProvider: MessageRealTimeProvider
    ): TabFlowComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onChatSelected: (chat: Chat) -> Unit,
            onUserSelected: (user: User) -> Unit,
            onLogout: () -> Unit,
            onEditProfile: () -> Unit,
            createNewChat: () -> Unit,
            createProfile: () -> Unit,
        ): TabFlowComponent {
            return TabFlowComponentImpl(
                componentContext,
                chatFlowComponentFactory,
                profileComponentFactory,
                usersListComponentFactory,
                messageRealtimeProvider,
                onChatSelected,
                onUserSelected,
                onLogout,
                createNewChat,
                onEditProfile,
                createProfile
            )
        }


    }

}