package com.example.uspower.features.chat.editchat

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.LoadState
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.isMainThread
import com.example.uspower.models.Chat
import com.example.uspower.models.FileAbstraction
import com.example.uspower.models.User
import com.example.uspower.notifications.NotificationSubscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job


class EditChatComponentImpl(
    componentContext: ComponentContext,
    private val chatRepository: ChatRepository,
    private val userRepository: UsersRepository,
    private val loginManager: LoginManager,
    private val notificationSubscriber: NotificationSubscriber,
    private val updateChatImageUseCase: UpdateChatImageUseCase,
    private val chat: Chat,
    private val goBack: () -> Unit,
    private val navigateToAddMembersToChat: (chat: Chat) -> Unit,
    private val onChatDeleted: () -> Unit
): ComponentContext by componentContext, EditChatComponent {

    val coroutineScope = componentCoroutineScope()
    override val chatName = MutableStateFlow(LoadState.Success(chat.name))
    override val chatMembers = MutableStateFlow<LoadState<List<User>>>(LoadState.Loading)
    override val adminOrOwner = MutableStateFlow(chat.owners?.contains(loginManager.user?.email) == true || loginManager.user?.admin == true)
    override val showDeleteChatConfirmationDialog = MutableStateFlow(false)
    override val chatAvatarState = MutableStateFlow(
        if (chat.photoUrl.isNotEmpty())
            FileAbstraction.Remote(chat.photoUrl)
        else FileAbstraction.None)
    override val showProgress = MutableStateFlow(false)

    private var chatSubscriptionJob: Job? = null

    init {
        chatSubscriptionJob = coroutineScope.launch(Dispatchers.IO) {
            chatRepository.subscribeToChat(chat).map<LoadState<Chat>, LoadState<List<User>>> { chatLoadState ->
                println("100500, chat laod state is $chatLoadState")
                when(chatLoadState) {
                    is LoadState.Error ->
                    {
                        throw (chatLoadState.throws)
                        LoadState.Error(chatLoadState.throws)
                    }
                    LoadState.Loading -> LoadState.Loading
                    is LoadState.Success -> {
                        println("100500, chat loaded")
                        val users = chatLoadState.data.participants?.map {
                            userRepository.getUserByEmail(it)
                        }
                        println("Finished load uesrs: ${users?.size}")

                        LoadState.Success(users?.filterNotNull() ?: emptyList())
                    }
                }
            }.collectLatest { users ->
                when (users) {
                    is LoadState.Error -> {chatMembers.value = LoadState.Error(users.throws)}
                    LoadState.Loading -> {chatMembers.value = LoadState.Loading}
                    is LoadState.Success -> {
                        chatMembers.value = LoadState.Success(users.data)
                    }
                }

            }

        }
    }

    override fun onDeleteChatClicked() {
        showDeleteChatConfirmationDialog.value = true

    }

    override fun onDeleteDialogDismissed() {
        showDeleteChatConfirmationDialog.value = false
    }

    override fun onDeleteChatConfirmed() {
        coroutineScope.launch(Dispatchers.IO) {
            showDeleteChatConfirmationDialog.value = false
            showProgress.value = true
            chatSubscriptionJob?.cancel()

            val users = userRepository.getUsers(true).filter { chat.participants?.contains(it.email) == true }

            users.forEach {
                notificationSubscriber.unsubscribeFromTopic(it.fcmToken, chat.chatId)
            }


            chatRepository.deleteChat(chat)
            showProgress.value = false
            onChatDeleted()
        }
    }

    override fun deleteUserFromChat(user: User) {
        println("100500 delete user from chatcall ")
        //TODO ANIMATION LOADING
        coroutineScope.launch(Dispatchers.IO) {
            showProgress.value = true
            println("100500 is mainThread ${isMainThread()}")
            notificationSubscriber.unsubscribeFromTopic(user.fcmToken, chat.chatId)
            chatRepository.removeUserFromChat(chat, user)
            showProgress.value = false
        }
    }

    override fun onChatNameChanged(newName: String) {
        //TODO debounce??? Animation
        chatName.value = LoadState.Success(newName)
        coroutineScope.launch(Dispatchers.IO) {
            chatRepository.renameChat(chat, newName)
        }
    }

    override fun backClicked() {
        goBack()
    }

    override fun addMembers() {
        navigateToAddMembersToChat(chat)
    }

    override fun showProgress(show: Boolean) {
        showProgress.value = show
    }


    override fun onImageCropped(byteArray: ByteArray) {
        chatAvatarState.value = FileAbstraction.CroppedImage(byteArray)

        coroutineScope.launch(Dispatchers.IO) {
            showProgress.value = true
            updateChatImage(byteArray)
            showProgress.value = false
        }
    }


    private suspend fun updateChatImage(byteArray: ByteArray) {
        updateChatImageUseCase.updateChatImage(byteArray, chat)
    }

    class Factory(
        private val chatRepository: ChatRepository,
        private val usersRepository: UsersRepository,
        private val loginManager: LoginManager,
        private val notificationSubscriber: NotificationSubscriber,
        private val updateChatImageUseCase: UpdateChatImageUseCase,
    ): EditChatComponent.Factory {
        override fun create(componentContext: ComponentContext, chat: Chat, goBack: () -> Unit, navigateToAddMembersToChat: (chat: Chat) -> Unit, onChatDeleted: () -> Unit): EditChatComponent {
            return EditChatComponentImpl(componentContext, chatRepository, usersRepository, loginManager, notificationSubscriber, updateChatImageUseCase, chat, goBack, navigateToAddMembersToChat, onChatDeleted)
        }

    }

}