package com.example.uspower.core.chat


import com.example.uspower.Log
import com.example.uspower.core.LoadState
import com.example.uspower.data.api.chats.supabase.SupabaseChatApi
import com.example.uspower.data.api.chats.supabase.entities.UnreadCountDto
import com.example.uspower.data.api.chats.supabase.entities.toChat
import com.example.uspower.data.datastore.LocalPreferences
import com.example.uspower.models.Chat
import com.example.uspower.models.User
import com.example.uspower.wrapToLoadState
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.serialization.json.Json
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ChatRepositoryImpl(
    private val supabaseChatApi: SupabaseChatApi,
    private val localPreferences: LocalPreferences,
): ChatRepository {

    override suspend fun getChats(user: User): List<Chat> {
        return supabaseChatApi.getChats(user.email).map { it.toChat() }
    }

    override fun subscribeToChats(user: User): Flow<LoadState<List<Chat>>> {
        return supabaseChatApi.getChatsFlow(user.email)
            .map { dtos ->
                Log("ChatRepository", "Emit data from api, dtos size is ${dtos.size}")
                val unreadCounts = supabaseChatApi.getCountOfUnreadMessagesForAllChats(user.uuid)
                Log("ChatRepository", "unreadCounts size is ${unreadCounts.size}")
                val chats = dtos.map { chatDto ->
                    val unreadCount = unreadCounts.firstOrNull { it.chatId == chatDto.chatid }?.unreadCount ?: 0
                    chatDto.toChat(unreadCount)

                }

                Log("ChatRepository", "return chats: ${chats.size}")
                chats
            }
            .onEach { chats -> localPreferences.saveChatsToList(Json.encodeToString(chats)) }
            .onStart {
                val cachedChats = localPreferences.getChats()
                Log("ChatRepository", "Cached data is ${cachedChats}")
                cachedChats?.let {
                    val realChats = Json.decodeFromString<List<Chat>>(it)
                    Log("ChatRepository", "Got chats from the cache and emit it ${realChats.size}")
                    emit(realChats)
                }
            }.wrapToLoadState()
    }

    override fun subscribeToChat(chat: Chat): Flow<LoadState<Chat>> {
        return supabaseChatApi.getChatFlow(chat.chatId).map { it.toChat() }.wrapToLoadState()
    }

    override suspend fun createChat(user: User, chatName: String, chatMembers: List<User>, ) {

        val createdChat = supabaseChatApi.createChat(Uuid.random().toString(), chatName)
        supabaseChatApi.setOwnerToChat(
            user.uuid,
            createdChat.uuid
        )

        supabaseChatApi.addMembersToChat(
            createdChat.uuid,
            chatMembers.map {
                it.uuid
            }.toMutableList().also { it.add(user.uuid) }
        )
//        println("Chat repository create chat")
//
//        val participants = chatMembers.map { it.email }.toMutableList()
//        participants.add(user.email)
//        val chatMap = hashMapOf(
//            "name" to chatName,
//            "photoUrl" to "",
//            "owners" to listOf(user.email),
//            "participants" to participants,
//            "messages" to mutableListOf<Message>(),
//            "global" to false
//        )
//
//        chatApi.createChat(chatMap)
    }

    override suspend fun deleteChat(chat: Chat) {
        supabaseChatApi.removeChat(chatId = chat.chatId)
    }

    override suspend fun addMembersToChat(chat: Chat, newMembers: List<User>) {
        supabaseChatApi.addMembersToChat(chat.chatId, newMembers.map { it.uuid })
    }

    override suspend fun removeUserFromChat(chat: Chat, user: User) {
        supabaseChatApi.removeMembersFromChat(chat.chatId, user.uuid)
    }

    override suspend fun renameChat(chat: Chat, newName: String) {
        supabaseChatApi.changeChatName(chat.chatId, newName)
    }

    override suspend fun updateChatPhoto(chat: Chat, photoUrl: String) {
        supabaseChatApi.updateChatPhoto(chatId = chat.chatId, photoUrl)
    }

    override suspend fun updateLastReadAt(user: User, chat: Chat, timestamp: Timestamp) {
        supabaseChatApi.updateLastReadAt(chat.chatId, user.uuid, Instant.fromEpochMilliseconds(timestamp.seconds*1000))
    }

    override suspend fun getCountOfUnreadMessages(user: User): List<UnreadCountDto> {
        return supabaseChatApi.getCountOfUnreadMessagesForAllChats(user.uuid).also {
            it.forEach {
                Log("ChatRepositoryImpl", "${it.chatId} - ${it.unreadCount}")
            }
        }
//        return supabaseChatApi.getCountOfUnreadMessages(chat.chatId, user.uuid)
    }

    override suspend fun removeCachedChats() {
        localPreferences.removeChatsFromPreferences()
    }
}