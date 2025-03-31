package com.example.uspower.features.mainflow

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popToFirst
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.arkivanov.essenty.lifecycle.doOnStop
import com.example.uspower.Log
import com.example.uspower.base.FlowComponentImpl
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.LoadState
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.login.LoginManager
import com.example.uspower.features.chat.chatroom.ChatRoomComponent
import com.example.uspower.features.chat.editchat.EditChatComponent
import com.example.uspower.features.chat.editchat.addmembers.AddMembersRootComponent
import com.example.uspower.features.createchat.NewChatFlowComponent
import com.example.uspower.features.editprofile.EditProfileComponent
import com.example.uspower.features.mainflow.MainFlowComponentImpl.Config
import com.example.uspower.features.profileflow.createprofile.CreateProfileComponent
import com.example.uspower.features.tabflow.TabFlowComponent
import com.example.uspower.features.singleuser.SingleUserComponent
import com.example.uspower.models.Chat
import com.example.uspower.models.User
import com.example.uspower.notifications.NotificationSubscriber
import com.example.uspower.permissions.PermissionControllerWrapper
import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer

class MainFlowComponentImpl(
    componentContext: ComponentContext,
    private val tabFlowComponentFactory: TabFlowComponent.Factory,
    private val chatRoomComponentFactory: ChatRoomComponent.Factory,
    private val singleUserComponentFactory: SingleUserComponent.Factory,
    private val editProfileComponentFactory: EditProfileComponent.Factory,
    private val newChatFlowComponentFactory: NewChatFlowComponent.Factory,
    private val editChatComponentFactory: EditChatComponent.Factory,
    private val addMembersRootComponentFactory: AddMembersRootComponent.Factory,
    private val createProfileComponentFactory: CreateProfileComponent.Factory,
    private val chatRepository: ChatRepository,
    private val loginManager: LoginManager,
    private val notificationSubscriber: NotificationSubscriber,
    private val permissionsController: PermissionControllerWrapper,
    private val onLogout: () -> Unit
): ComponentContext by componentContext, MainFlowComponent,
    FlowComponentImpl<MainFlowComponent.Child, Config>() {
        val coroutineScope = componentCoroutineScope()
        init {
            println("100500 create main flow")

            lifecycle.doOnStop {
                Log("MainFlowComponent", "On stop")
            }
            lifecycle.doOnDestroy {
                Log("MainFlowComponent", "On destroy")
            }

            coroutineScope.launch(Dispatchers.IO) {
                loginManager.user?.let { user ->

                    val token = NotifierManager.getPushNotifier().getToken()
//                    val token = Firebase.messaging.getToken()
                    println("100500 update token, token is $token")
                    token?.let {
                        loginManager.updateToken(token)
                    }

                    chatRepository.subscribeToChats(user).collect() { chats ->

                        when(chats) {
                            is LoadState.Error -> {
                                //TODO
                            }
                            LoadState.Loading -> {
                                //TODO
                            }
                            is LoadState.Success -> {
                                notificationSubscriber.subscribeToTopics(chats.data.map { it.chatId })
                            }
                        }

                    }
                }



            }


        }

    override val initialConfiguration: Config = Config.TabFlow

    override val configSerializer: KSerializer<Config> = Config.serializer()
    override val handleBack: Boolean = true

    fun onChatSelected(chat: Chat) {
        println("Root component chat selected")
        nav.push(
            configuration = Config.ChatRoom(chat)
        )
    }

    fun onEditProfileClicked() {
        println("On edit profile clicked")
        nav.push(
            configuration = Config.EditProfile
        )
    }

    fun onUserSelected(user: User) {
        println("On user selected")
        nav.push(
            configuration = Config.UserScreen(user)
        )
    }

    fun goBack() {
        println("Go back called")
        nav.pop()
    }

    fun onLogoutConfirmed() {
        onLogout()
    }

    fun createNewChat() {
        nav.push(
            configuration = Config.NewChatFlow
        )
    }

    fun goToEditChat(chat: Chat) {
        nav.push(
            configuration = Config.EditChat(chat)
        )
    }

    fun onChatDeleted() {
        nav.popToFirst()
    }

    fun navigateToAddMembersToChat(chat: Chat) {
        nav.push(Config.AddMembersToExistingChat(chat))
    }

    fun createProfile() {
        nav.push(Config.CreateProfile)
    }



    override fun child(
        config: Config,
        componentContext: ComponentContext
    ): MainFlowComponent.Child {
        return when(config) {

            Config.TabFlow -> MainFlowComponent.Child.TabFlow(
                tabFlowComponentFactory.create(componentContext, ::onChatSelected, ::onUserSelected, ::onLogoutConfirmed, ::onEditProfileClicked, ::createNewChat, ::createProfile)
            )

            is Config.ChatRoom -> MainFlowComponent.Child.ChatRoom(
                chatRoomComponentFactory.create(componentContext, config.chat, permissionsController, ::goBack, ::onUserSelected, ::goToEditChat)
            )

            is Config.UserScreen -> MainFlowComponent.Child.UserScreen(
                singleUserComponentFactory.create(componentContext, config.user, ::goBack)
            )

            Config.EditProfile -> MainFlowComponent.Child.EditProfile(
                editProfileComponentFactory.create(componentContext, ::goBack)
            )

            Config.NewChatFlow -> MainFlowComponent.Child.CreateChatFlow(
                newChatFlowComponentFactory.create(componentContext, ::goBack)
            )

            is Config.EditChat -> MainFlowComponent.Child.EditChat(
                editChatComponentFactory.create(componentContext, config.chat, ::goBack, ::navigateToAddMembersToChat, ::onChatDeleted)
            )

            is Config.AddMembersToExistingChat -> MainFlowComponent.Child.AddMembersToExistingChat(
                addMembersRootComponentFactory.create(componentContext, config.chat, ::goBack)
            )

            Config.CreateProfile -> MainFlowComponent.Child.CreateProfile(createProfileComponentFactory.create(componentContext, ::goBack))
        }
    }


    @kotlinx.serialization.Serializable
    sealed interface Config {
        @kotlinx.serialization.Serializable
        data object TabFlow : Config

        @kotlinx.serialization.Serializable
        data class ChatRoom(val chat: Chat) : Config

        @kotlinx.serialization.Serializable
        data class UserScreen(val user: User) : Config

        @kotlinx.serialization.Serializable
        data object EditProfile : Config

        @kotlinx.serialization.Serializable
        data object CreateProfile : Config

        @kotlinx.serialization.Serializable
        data object NewChatFlow : Config

        @kotlinx.serialization.Serializable
        data class EditChat(val chat: Chat) : Config

        @kotlinx.serialization.Serializable
        data class AddMembersToExistingChat(val chat: Chat) : Config



    }

    class Factory(
        private val tabFlowComponentFactory: TabFlowComponent.Factory,
        private val chatRoomFactory: ChatRoomComponent.Factory,
        private val singleUserComponentFactory: SingleUserComponent.Factory,
        private val editProfileComponentFactory: EditProfileComponent.Factory,
        private val newChatFlowComponentFactory: NewChatFlowComponent.Factory,
        private val editChatComponentFactory: EditChatComponent.Factory,
        private val addMembersRootComponentFactory: AddMembersRootComponent.Factory,
        private val createProfileComponentFactory: CreateProfileComponent.Factory,
        private val chatRepository: ChatRepository,
        private val loginManager: LoginManager,
        private val notificationSubscriber: NotificationSubscriber,
    ): MainFlowComponent.Factory {
        override fun create(componentContext: ComponentContext, permissionsController: PermissionControllerWrapper, onLogout: () -> Unit): MainFlowComponent {
            return MainFlowComponentImpl(
                componentContext,
                tabFlowComponentFactory,
                chatRoomFactory,
                singleUserComponentFactory,
                editProfileComponentFactory,
                newChatFlowComponentFactory,
                editChatComponentFactory,
                addMembersRootComponentFactory,
                createProfileComponentFactory,
                chatRepository,
                loginManager,
                notificationSubscriber,
                permissionsController,
                onLogout,
            )
        }

    }

}