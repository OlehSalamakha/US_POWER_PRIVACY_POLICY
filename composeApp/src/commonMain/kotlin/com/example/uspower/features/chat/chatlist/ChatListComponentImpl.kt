package com.example.uspower.features.chat.chatlist

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.essenty.lifecycle.doOnStart
import com.example.uspower.Log
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.LoadState
import com.example.uspower.core.login.LoginManager
import com.example.uspower.data.api.chats.supabase.SupabaseChatApi
import com.example.uspower.data.api.chats.supabase.entities.UnreadCountDto
import com.example.uspower.features.chat.chatlist.data.ChatLastMessage
import com.example.uspower.features.chat.chatlist.data.ChatListProvider
import com.example.uspower.models.Chat
import dev.gitlive.firebase.firestore.toMilliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatListComponentImpl(
    componentContext: ComponentContext,
    private val chatListProvider: ChatListProvider,
    private val onChatSelected: (chat: Chat) -> Unit,
    private val createNewChat: () -> Unit,

): ComponentContext by componentContext, ChatListComponent {

    private val coroutineScope = componentCoroutineScope()

    override val chatSearch = MutableStateFlow("")


    private val unreadCountsFlow = MutableStateFlow<List<UnreadCountDto>>(emptyList())
    init {
        Log("ChatListComponentImpl", "init")

        lifecycle.doOnResume {
            coroutineScope.launch(Dispatchers.IO) {
                Log("ChatListComponentImpl", "onstart")
                fetchUnreadCount()
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val uiChats: StateFlow<LoadState<List<Chat>>> =
        combine(
            chatListProvider.subscribeToChat(),
            unreadCountsFlow,
            chatSearch
        ) { chatLoadState, unreadCounts, query ->
            if (chatLoadState is LoadState.Success) {
                fetchUnreadCount() // Fetch unread counts whenever chat list updates
            }


            Log("ChatListcomponentImpl", "Chat load state is ${chatLoadState}")
            val sortedChats = when (chatLoadState) {
                is LoadState.Loading -> return@combine LoadState.Loading
                is LoadState.Error -> return@combine LoadState.Error(chatLoadState.throws)
                is LoadState.Success -> {

                    chatLoadState.data.sortedByDescending { it.lastMessageTimeStamp?.toMilliseconds() ?: Double.MIN_VALUE }
                        .map { chat ->
                            Log("100900", "Last message timestamp for ${chat.chatId} - ${chat.name} - ${chat.lastMessageTimeStamp?.seconds?.times(
                                1000
                            )}")
                            chat.copy(unreadCount = unreadCounts.firstOrNull { it.chatId == chat.chatId }?.unreadCount ?: 0) // Update unread count
                        }
                }
            }

            if (query.isBlank()) {
                Log("ChatListcomponentImpl", "Emit sorted chats: ${sortedChats.size}")
                LoadState.Success(sortedChats)
            } else {
                LoadState.Success(sortedChats.filter { it.name.contains(query, ignoreCase = true) })
            }
        }
            .flowOn(Dispatchers.IO)
            .stateIn(coroutineScope, SharingStarted.Eagerly, LoadState.Loading)


    override fun onChatClicked(chat: Chat) {
        onChatSelected(chat)
    }

    override fun onCreateNewChatClicked() {
        createNewChat()
    }

    override fun onSearchChanged(query: String) {
        chatSearch.value = query
    }

    private suspend fun fetchUnreadCount()  {
        val countOfUnread = chatListProvider.getCountOfUnread()
        unreadCountsFlow.value = countOfUnread
    }


    class Factory(
        private val chatListProvider: ChatListProvider,
    ): ChatListComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onChatSelected: (chat: Chat) -> Unit,
            createNewChat: () -> Unit
        ): ChatListComponent {
            return ChatListComponentImpl(componentContext, chatListProvider, onChatSelected, createNewChat)
        }

    }
}

