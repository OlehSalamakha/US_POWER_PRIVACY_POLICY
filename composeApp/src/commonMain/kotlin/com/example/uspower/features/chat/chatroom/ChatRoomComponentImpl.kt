package com.example.uspower.features.chat.chatroom

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnPause
import com.example.uspower.Log
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.messages.MessageProvider
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.data.api.files.FilesApi
import com.example.uspower.core.LoadState
import com.example.uspower.getDateText
import com.example.uspower.isMainThread
import com.example.uspower.models.Chat
import com.example.uspower.models.FileAbstraction
import com.example.uspower.models.LOADING_IMAGE
import com.example.uspower.models.Message
import com.example.uspower.models.ReactionEmoji
import com.example.uspower.models.User
import com.example.uspower.openFile
import com.example.uspower.permissions.DeniedAlwaysException
import com.example.uspower.permissions.DeniedException
import com.example.uspower.permissions.PermissionControllerWrapper
import com.example.uspower.shouldShowDate
import com.example.uspower.toByteArray
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatRoomComponentImpl(
    componentContext: ComponentContext,
    private val messageProvider: MessageProvider,
    private val chatRepository: ChatRepository,
    private val loginManager: LoginManager,
    private val userManager: UsersRepository,
    private val filesApi: FilesApi,
    private val controller: PermissionControllerWrapper,
    private val externalCoroutineScope: CoroutineScope,
    val chat: Chat,
    private val goBack: () -> Unit,
    private val onUserSelected: (user: User) -> Unit,
    private val goToEditChat: (chat: Chat) -> Unit

) : ComponentContext by componentContext, ChatRoomComponent {

    override val messages = mutableStateListOf<Message>()
    override val isLoad: MutableState<Boolean> = mutableStateOf(false)
    override val isDownloadFile: MutableState<Boolean> = mutableStateOf(false)
    override val chatFlow = MutableStateFlow(chat)
    override val selectedMessage: MutableState<Message?> = mutableStateOf(null)
    override fun onGoBackClicked() {
        goBack()
    }

    override val imageState: MutableStateFlow<FileAbstraction> = MutableStateFlow(FileAbstraction.None)
    override val cameraShowState = MutableStateFlow(false)
    override val fileDownloadedSrtate = MutableSharedFlow<OpenFileEvent>(replay = 0)

    @OptIn(ExperimentalUuidApi::class)
    override fun onImageLoaded(file: PlatformFile) {
        val messageId = Uuid.random().toString()
        coroutineScope.launch(Dispatchers.IO) {
            messages.add(
                0,
                Message(
                    messageId = messageId,
                    sender = loginManager.user?.uuid.toString(),
                    content = "",
                    timestamp = Timestamp.now(),
                    type = LOADING_IMAGE,
                    fileName = "",
                    reactions = mutableListOf(),
                    isFromMe = true,
                    chatId = chat.chatId
                )
            )
            filesApi.uploadFile(file).collect { uploadState ->
                when (uploadState) {
                    is LoadState.Error -> {
                        messages.removeAll { it.messageId == messageId }
                    }

                    LoadState.Loading -> {

                    }

                    is LoadState.Success -> {
                        val imageUrl = uploadState.data
                        val newMessage = Message(
                            messageId = messageId,
                            sender = loginManager.user?.uuid.toString(),
                            content = imageUrl,
                            timestamp = Timestamp.now(),
                            type = 2,
                            fileName = "",
                            reactions = mutableListOf(),
                            isFromMe = true,
                            chatId = chat.chatId
                        )
                        messageProvider.sendMessage(chat.chatId, newMessage)
                    }
                }
            }
        }
        println("100500 File path issss ${file.path}")
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onFileLoaded(file: PlatformFile) {
        val messageId = Uuid.random().toString()
        coroutineScope.launch(Dispatchers.IO) {
            messages.add(
                0,
                Message(
                    messageId = messageId,
                    sender = loginManager.user?.uuid.toString(),
                    content = "",
                    timestamp = Timestamp.now(),
                    type = LOADING_IMAGE,
                    fileName = "",
                    reactions = mutableListOf(),
                    isFromMe = true,
                    chatId = chat.chatId
                )
            )
            filesApi.uploadFile(file).collect { uploadState ->
                when (uploadState) {
                    is LoadState.Error -> {

                    }

                    LoadState.Loading -> {

                    }

                    is LoadState.Success -> {
                        val imageUrl = uploadState.data
                        val newMessage = Message(
                            messageId = Uuid.random().toString(),
                            sender = loginManager.user?.uuid.toString(),
                            content = imageUrl,
                            timestamp = Timestamp.now(),
                            type = 3,
                            fileName = file.name,
                            reactions = mutableListOf(),
                            isFromMe = true,
                            chatId = chat.chatId
                        )

                        messageProvider.sendMessage(chat.chatId, newMessage)
                    }
                }
            }
        }
        println("100500 File path issss ${file.path}")
    }

    private val coroutineScope = componentCoroutineScope()


    init {



        lifecycle.doOnPause {
            runBlocking {
                loginManager.user?.let { user ->
                    Log("ChatRoomComponentIMpl", "user not null")
                    val lastMessage = messages.firstOrNull()

                    lastMessage?.let {
                        val timestamp = it.timestamp.seconds*1000 +2000
                        chatRepository.updateLastReadAt(user, chat, Timestamp.fromMilliseconds(timestamp.toDouble()))
                    } ?: run {
                        chatRepository.updateLastReadAt(user, chat, Timestamp.now())
                    }

                    Log("ChatRoomComponentIMpl", "Finished update last read at")
                }
            }
        }
        coroutineScope.launch {


            loginManager.user?.let {
                chatRepository.updateLastReadAt(it, chat, Timestamp.now())
            }

            fileDownloadedSrtate
                .filter {
                    it.path.isNotEmpty() && it.fileName.isNotEmpty()
                }
                .collectLatest {
                    openFile(filePath = it.path, fileName = it.fileName)
                }
        }

        coroutineScope.launch {
            chatRepository.subscribeToChat(chat)
                .catch {
                    println("ChatRoomComponentImpl, catch error $it")
                }
                .collectLatest {
                    when (it) {
                        is LoadState.Error -> {
                            //TODO
                        }

                        LoadState.Loading -> {
                            //TODO
                        }

                        is LoadState.Success -> {
                            chatFlow.value = it.data
                        }
                    }

                }


        }
        coroutineScope.launch(Dispatchers.IO) {
            subscribe()
        }
        coroutineScope.launch(Dispatchers.IO) {
            subscribeRemoved()
        }
    }

    override suspend fun loadMessages(message: Message?) {
        if (message == null) {
            isLoad.value = true
        }
        val newMessages = messageProvider.getMessagesWithPagination(chat.chatId, message)
            .filter { messages.none { existing -> existing.messageId == it.messageId } }
            .map { it.copy(isFromMe = it.sender == loginManager.user!!.uuid) }
            .map { it.copy(date = if (it.isFirstMessageOfTheDay) getDateText(it.timestamp) else null) }

        messages.addAll(newMessages)
        if (message == null) {
            isLoad.value = false
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun sendMessage(message: String) {
        val newMessage = Message(
            messageId = Uuid.random().toString(),
            sender = loginManager.user?.uuid.toString(),
            content = message,
            timestamp = Timestamp.now(),
            type = 1,
            fileName = "",
            reactions = mutableListOf(),
            isFromMe = true,
            chatId = chat.chatId
        )
        coroutineScope.launch(Dispatchers.IO) {
            messageProvider.sendMessage(chat.chatId, newMessage)
        }
    }

    override suspend fun subscribe() {
        println("100500 is main thread subscribe: ${isMainThread()}")
        messageProvider.subscribeToMessages(chat.chatId).collect { newMessages ->
            println("1005005 newMessages: ${newMessages}")
            newMessages.forEach { newMessage ->
                messages.find { it.messageId == newMessage.messageId }?.let {
                    val indexOf = messages.indexOf(it)
                    if (indexOf != -1) {
                        messages.removeAt(indexOf)
                        messages.add(indexOf, newMessage)
                    }
                } ?: let {
                    val lastMessageDate = messages.maxOfOrNull { it.timestamp.toMilliseconds() } ?: 0L
                    if (newMessage.timestamp.toMilliseconds() > lastMessageDate.toDouble()) {
                        messages.removeAll { it.type == LOADING_IMAGE }
                        messages.add(0, newMessage.copy(date = if (newMessage.isFirstMessageOfTheDay) getDateText(newMessage.timestamp) else null))
                    }
                }
            }
        }

    }

    override fun onUserClicked(userMail: String) {
        coroutineScope.launch {
            val user = userManager.getUserByEmail(userMail)
            user?.let {
                onUserSelected(it)
            }
        }
    }

    override fun onEditChatClicked() {
        goToEditChat(chat)
    }

    override suspend fun subscribeRemoved() {
        messageProvider.subscribeToRemovedMessages(chat.chatId).collect {
            it.forEach {
                messages.removeAll { message ->
                    it.messageId == message.messageId
                }
            }
        }
    }

    override fun addEmoji(emoji: ReactionEmoji, message: Message?) {
        message?.apply {
            coroutineScope.launch {
                messageProvider.addOrRemoveEmoji(chat.chatId, emoji, this@apply)
            }
        }
    }

    override fun showPrimaryBackground(emoji: ReactionEmoji): Boolean {
        return selectedMessage.value?.reactions?.any { reaction -> reaction.content == emoji.code && reaction.sender == loginManager.user?.uuid } == true
    }

    override fun deleteMessage(selectedMessage: Message?) {
        selectedMessage?.apply {
            coroutineScope.launch {
                messages.remove(selectedMessage)
                messageProvider.removeMessage(chat.chatId, this@apply)
            }
        }
    }

    override fun openFileFromChat(fileName: String, path: String) {
        openFile(path, fileName)
    }

    override fun askCameraPermission() {
        coroutineScope.launch {
            try {
                controller.providePermission()
                cameraShowState.value = true
                println("100500, permissin granted")
            } catch (deniedAlways: DeniedAlwaysException) {
                cameraShowState.value = false
            } catch (denied: DeniedException) {
                cameraShowState.value = false
            }

        }

    }

    @OptIn(ExperimentalUuidApi::class)
    override fun onPhotoTaken(photo: ImageBitmap) {
        cameraShowState.value = false
        val messageId = Uuid.random().toString()
        messages.add(
            0,
            Message(
                messageId = messageId,
                sender = loginManager.user?.uuid.toString(),
                content = "",
                timestamp = Timestamp.now(),
                type = LOADING_IMAGE,
                fileName = "",
                reactions = mutableListOf(),
                isFromMe = true,
                chatId = chat.chatId
            )
        )
        coroutineScope.launch {

            filesApi.uploadCroppedPhoto(photo.toByteArray()).collect { uploadState ->
                when (uploadState) {
                    is LoadState.Error -> {

                    }

                    LoadState.Loading -> {

                    }

                    is LoadState.Success -> {
                        val imageUrl = uploadState.data
                        val newMessage = Message(
                            messageId = Uuid.random().toString(),
                            sender = loginManager.user?.uuid.toString(),
                            content = imageUrl,
                            timestamp = Timestamp.now(),
                            type = 2,
                            fileName = "",
                            reactions = mutableListOf(),
                            isFromMe = true,
                            chatId = chat.chatId
                        )

                        messageProvider.sendMessage(chat.chatId, newMessage)
                    }
                }
            }
        }
    }

    override fun resetCamera() {
        cameraShowState.value = false
    }

    override fun downloadAndOpen(message: Message) {
        /*TODO
            CHECK IF FILE DOWNLOADED
         */
        isDownloadFile.value = true
        coroutineScope.launch(Dispatchers.IO) {
            filesApi.downloadFile(message.content, message.fileName).collect {
                when (it) {
                    is LoadState.Error -> {
                        isDownloadFile.value = false
                        /*
                            TODO HANDLE ERRORS
                         */
                    }

                    LoadState.Loading -> {
                        /*
                            TODO HANDLE LOADING
                         */
                    }

                    is LoadState.Success -> {
                        isDownloadFile.value = false
                        fileDownloadedSrtate.emit(OpenFileEvent(message.fileName, it.data))
                        println("100500 success will open file ${it.data}, ${message.fileName}")
//                        fileDownloadedSrtate.value = OpenFileEvent(message.fileName, it.data)
                    }

                }
            }
        }
    }


    class Factory(
        private val messageProvider: MessageProvider,
        private val loginManager: LoginManager,
        private val usersRepository: UsersRepository,
        private val chatRepository: ChatRepository,
        private val filesApi: FilesApi,
        private val externalCoroutineScope: CoroutineScope,
    ) : ChatRoomComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            chat: Chat,
            controller: PermissionControllerWrapper,
            goBack: () -> Unit,
            onUserSelected: (user: User) -> Unit,
            goToEditChat: (chat: Chat) -> Unit
        ): ChatRoomComponent {
            return ChatRoomComponentImpl(componentContext, messageProvider, chatRepository, loginManager, usersRepository, filesApi, controller, externalCoroutineScope, chat, goBack, onUserSelected, goToEditChat)
        }
    }

}

data class OpenFileEvent(
    val fileName: String,
    val path: String,
)
