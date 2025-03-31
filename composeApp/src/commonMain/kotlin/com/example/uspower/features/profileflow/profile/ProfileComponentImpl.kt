package com.example.uspower.features.profileflow.profile

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.login.LoginManager
import com.example.uspower.notifications.NotificationSubscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ProfileComponentImpl(
    componentContext: ComponentContext,
    private val notificationSubscriber: NotificationSubscriber,
    private val chatRepository: ChatRepository,
    private val loginManager: LoginManager,
    private val onLogout: () -> Unit,
    private val onEditProfileClicked: () -> Unit,
    private val createProfile: () -> Unit
): ComponentContext by componentContext, ProfileComponent {
    val coroutineScope = componentCoroutineScope()


    override val user = loginManager.getCurrentUserFlow().stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = loginManager.user!!

    )
    override val showDialog = MutableStateFlow(DialogState(false, "", "", {}, {}))


    override fun onLogoutClicked() {
        showDialog.value = DialogState(
            show = true,
            title = "Log Out",
            label = "Do you want to log out?",
            ::onLogoutConfirmed,
            ::onLogoutDialogDismissed
        )
    }

    override fun onLogoutConfirmed() {
        coroutineScope.launch {
            val user = loginManager.user
            user?.let {
                val topics = chatRepository.getChats(user).map { it.chatId }
                notificationSubscriber.unsubscribeFromTopics(user.fcmToken, topics)
            }

            loginManager.logout()
            onLogout()
        }

    }

    override fun onLogoutDialogDismissed() {
       showDialog.value = DialogState(
           show = false,
           title = "Log Out",
           label = "Do you want to log out?",
           ::onLogoutConfirmed,
           ::onLogoutDialogDismissed
       )
    }

    override fun onDeleteProfileClicked() {
       showDialog.value = DialogState(
           show = true,
           title = "Delete Account",
           label = "Do you want to delete your account?",
           ::onDeleteProfileConfirmed,
           ::onLogoutDialogDismissed
       )
    }

    override fun onDeleteProfileConfirmed() {
        coroutineScope.launch  {
            loginManager.deleteProfile()
            onLogout()
        }

    }

    override fun onDeleteDialogDismissed() {
        showDialog.value = DialogState(
            show = false,
            title = "Delete Account",
            label = "Do you want to delete your account?",
            ::onDeleteProfileConfirmed,
            ::onLogoutDialogDismissed
        )
    }

    override fun onEditProfile() {
        onEditProfileClicked()
    }

    override fun onCreateProfile() {
        createProfile()
    }


    class Factory(
        private val loginManager: LoginManager,
        private val notificationSubscriber: NotificationSubscriber,
        private val chatRepository: ChatRepository,
    ): ProfileComponent.Factory {
        override fun create(componentContext: ComponentContext, onLogout: () -> Unit, onEditProfileClicked: () -> Unit, onCreateProfile: () -> Unit): ProfileComponent {
            return ProfileComponentImpl(componentContext, notificationSubscriber, chatRepository, loginManager, onLogout, onEditProfileClicked, onCreateProfile)
        }
    }

}
