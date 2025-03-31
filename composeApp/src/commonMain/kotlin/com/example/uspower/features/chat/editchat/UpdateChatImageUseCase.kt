package com.example.uspower.features.chat.editchat

import com.example.uspower.core.LoadState
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.data.api.files.FilesApi
import com.example.uspower.models.Chat

class UpdateChatImageUseCase(
    private val filesApi: FilesApi,
    private val chatRepository: ChatRepository
) {

    suspend fun updateChatImage(
        newImage: ByteArray,
        chat: Chat
    ) {
        filesApi.uploadCroppedPhoto(newImage).collect { uploadResult ->
            when (uploadResult) {
                is LoadState.Error -> {

                }
                LoadState.Loading -> {}
                is LoadState.Success -> {
                    val imageUrl = uploadResult.data
                    updateChatWithUrl(imageUrl, chat)
                }
            }
        }
    }


    private suspend fun updateChatWithUrl(url: String, chat: Chat) {
        chatRepository.updateChatPhoto(chat, url)
    }
}