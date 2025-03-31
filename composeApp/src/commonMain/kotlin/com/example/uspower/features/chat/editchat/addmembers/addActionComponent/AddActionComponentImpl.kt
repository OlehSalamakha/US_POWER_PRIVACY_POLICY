package com.example.uspower.features.chat.editchat.addmembers.addActionComponent

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.models.Chat
import com.example.uspower.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddActionComponentImpl(
    componentContext: ComponentContext,
    private val chatRepository: ChatRepository,
    private val selectedUsers: StateFlow<Set<User>>,
    val chat: Chat,
    val goBack: () -> Unit,
    val showProgress: (Boolean) -> Unit
): ComponentContext by componentContext, AddActionComponent {

    private val coroutineScope = componentCoroutineScope()

    override val enableAddButton: StateFlow<Boolean> = selectedUsers.map {
        it.isNotEmpty()
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), selectedUsers.value.isNotEmpty())


    override fun addMembers() {
        coroutineScope.launch(Dispatchers.IO) {
            showProgress(true)
            chatRepository.addMembersToChat(chat, selectedUsers.value.toList())
            showProgress(false)
            goBack()
        }

    }


    class Factory(
        val chatRepository: ChatRepository
    ): AddActionComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            chat: Chat,
            selectedUsers: StateFlow<Set<User>>,
            goBack: () -> Unit,
            showProgress: (Boolean) -> Unit,
        ): AddActionComponent {
            return AddActionComponentImpl(componentContext, chatRepository, selectedUsers, chat, goBack, showProgress)
        }

    }





}