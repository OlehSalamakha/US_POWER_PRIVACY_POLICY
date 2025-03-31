package com.example.uspower.features.createchat.create

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.login.LoginManager
import com.example.uspower.models.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CreateChatComponentImpl(
    componentContext: ComponentContext,
    private val loginManager: LoginManager,
    private val chatName: String,
    private val users: StateFlow<Set<User>>,
    private val chatRepository: ChatRepository,
    private val goBack: () -> Unit,
    private val showProgress: (Boolean) -> Unit

): ComponentContext by componentContext, CreateChatComponent {


    private val coroutineScope = componentCoroutineScope()
    override val enabledCreationButton: StateFlow<Boolean> = users.map {
        it.isNotEmpty()
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), users.value.isNotEmpty())


    override fun createChat() {
        coroutineScope.launch {
            showProgress(true)
            chatRepository.createChat(loginManager.user!!, chatName, users.value.toList())
            showProgress(false)
            goBack()
        }
    }

    class Factory(
        private val loginManager: LoginManager,
        private val chatRepository: ChatRepository
    ): CreateChatComponent.Factory {

        override fun create(
            componentContext: ComponentContext,
            chatName: String,
            usersFlow: StateFlow<Set<User>>,
            goBack: () -> Unit,
            showProgress: (Boolean) -> Unit
        ): CreateChatComponent {
            return CreateChatComponentImpl(
                componentContext,
                loginManager,
                chatName,
                usersFlow,
                chatRepository,
                goBack,
                showProgress
            )
        }

    }
}