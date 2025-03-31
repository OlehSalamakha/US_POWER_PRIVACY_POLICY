package com.example.uspower.features.chat.chatlist

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.core.LoadState
import com.example.uspower.features.chat.chatlist.data.ChatLastMessage
import com.example.uspower.models.Chat
import kotlinx.coroutines.flow.StateFlow

interface ChatListComponent {

    val uiChats: StateFlow<LoadState<List<Chat>>>

    val chatSearch: StateFlow<String>

    fun onChatClicked(chat: Chat)

    fun onCreateNewChatClicked()

    fun onSearchChanged(query: String)

    interface Factory {
        fun create(componentContext: ComponentContext, onChatSelected: (chat: Chat) -> Unit, createNewChat: () -> Unit): ChatListComponent
    }
}