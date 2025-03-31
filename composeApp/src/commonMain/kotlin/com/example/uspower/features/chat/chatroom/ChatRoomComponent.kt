package com.example.uspower.features.chat.chatroom

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import com.arkivanov.decompose.ComponentContext
import com.example.uspower.models.Chat
import com.example.uspower.models.FileAbstraction
import com.example.uspower.models.Message
import com.example.uspower.models.ReactionEmoji
import com.example.uspower.models.User
import com.example.uspower.permissions.PermissionControllerWrapper
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharedFlow


interface ChatRoomComponent {
    val messages: SnapshotStateList<Message>
    val isLoad: MutableState<Boolean>
    val isDownloadFile: MutableState<Boolean>
    val chatFlow: StateFlow<Chat>
    val selectedMessage: MutableState<Message?>

    val imageState: StateFlow<FileAbstraction>

    val cameraShowState: StateFlow<Boolean>

    val fileDownloadedSrtate: SharedFlow<OpenFileEvent>
    fun onImageLoaded(file: PlatformFile)
    fun onFileLoaded(file: PlatformFile)



    suspend fun loadMessages(message: Message? = null)
    fun sendMessage(message: String)
    suspend fun subscribe()
    suspend fun subscribeRemoved()
    fun addEmoji(emoji: ReactionEmoji, message: Message?)


    fun showPrimaryBackground(emoji: ReactionEmoji): Boolean

    fun onUserClicked(userMail: String)

    fun onEditChatClicked()

    fun onGoBackClicked()
    fun deleteMessage(selectedMessage: Message?)
    fun downloadAndOpen(message: Message)

    fun openFileFromChat(fileName: String, path: String)

    fun askCameraPermission()
    fun onPhotoTaken(photo: ImageBitmap)
    fun resetCamera()

    interface Factory {
        fun create(componentContext: ComponentContext, chat: Chat, controller: PermissionControllerWrapper, goBack: () -> Unit, onUserSelected: (user: User) -> Unit, goToEditChat: (chat: Chat) -> Unit): ChatRoomComponent
    }
}