package com.example.uspower.features.chat.editchat

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.core.LoadState
import com.example.uspower.models.Chat
import com.example.uspower.models.FileAbstraction
import com.example.uspower.models.User
import kotlinx.coroutines.flow.StateFlow

interface EditChatComponent {
    val chatName: StateFlow<LoadState<String>>
    val chatMembers: StateFlow<LoadState<List<User>>>
    val adminOrOwner: StateFlow<Boolean>
    val showDeleteChatConfirmationDialog: StateFlow<Boolean>

    val chatAvatarState: StateFlow<FileAbstraction>

    val showProgress: StateFlow<Boolean>

    fun onDeleteChatClicked()
    fun onDeleteDialogDismissed()
    fun onDeleteChatConfirmed()
    fun deleteUserFromChat(user: User)
    fun onChatNameChanged(newName: String)
    fun backClicked()

    fun addMembers()

    fun showProgress(show: Boolean)

    fun onImageCropped(byteArray: ByteArray)


    interface Factory {
        fun create(
            componentContext: ComponentContext,
            chat: Chat,
            goBack: () -> Unit,
            navigateToAddMembersToChat: (chat: Chat) -> Unit,
            onChatDeleted: () -> Unit
        ): EditChatComponent
    }
}