package com.example.uspower.core.messages.realtime

import com.example.uspower.core.LoadState
import com.example.uspower.core.chat.ChatRepository
import com.example.uspower.core.login.LoginManager
import com.example.uspower.data.api.messages.MessageDTO
import com.example.uspower.Log
import com.example.uspower.core.messages.MessageCache
import com.example.uspower.data.api.messages.toMessage
import com.example.uspower.models.Chat
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.serialization.json.Json
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MessageRealTimeProviderImpl(
    val loginManager: LoginManager,
    val chatRepository: ChatRepository,
    val coroutineScope: CoroutineScope,
    val supabaseClient: SupabaseClient,
    private val messageCache: MessageCache
):MessageRealTimeProvider  {
    private val TAG  = "MessageRealTimeProvider"


    private var channel = supabaseClient.channel("MessageRealTimeProvider")

    private val messageFlow: Flow<MessageRealTimeEvent> by lazy {
        subscribeToAllMessages()
    }


    init {
        Log("100500", "Create MessageRealTimeProviderImpl")
       channel.status.onEach {
           Log(TAG, "Cnanle status is $it")
       }.launchIn(coroutineScope)

    }



    override fun subscribeToChat(chatId: String): Flow<MessageRealTimeEvent> {
        Log(TAG, "Subscribed, return flow")
        return messageFlow.filter {
            it.message?.chatId == chatId
        }
    }

    @OptIn(ExperimentalUuidApi::class, ExperimentalCoroutinesApi::class)
    private fun subscribeToAllMessages(): Flow<MessageRealTimeEvent> {
        return loginManager.getCurrentUserFlow()
            .flatMapLatest { chatRepository.subscribeToChats(it) }
            .filterIsInstance<LoadState.Success<List<Chat>>>()
            .map { it.data.map { chat -> chat.chatId } }
            .distinctUntilChanged()
            .flatMapLatest { chatIds ->
                Log(TAG, "Subscribed to chat IDs: $chatIds")

                // Remove old channel & create a new one
                supabaseClient.realtime.removeChannel(channel)
                channel = supabaseClient.channel(Uuid.random().toString())

                // Subscribe to message changes in those chats
                val flow = channel.postgresChangeFlow<PostgresAction>("public") {
                    table = "Messages"
                    filter("chatid", FilterOperator.IN, chatIds)
                }.filter { it !is PostgresAction.Select}

                channel.subscribe(true)
                flow
            }
            .map { data ->

                when(data) {
                    is PostgresAction.Delete -> {
                        Log(TAG, "Received deleteeeeee")
                        val message = Json.decodeFromString<MessageDTO>(data.oldRecord.toString()).toMessage()
                        Log(TAG, "Received DELETE message: $message")
                        messageCache.deleteFromCache( mapOf(message.chatId to mapOf(message.messageId to message)))
                        MessageRealTimeEvent.Delete(message)
                    }
                    is PostgresAction.Insert -> {
                        val message = Json.decodeFromString<MessageDTO>(data.record.toString()).toMessage()
                        Log(TAG, "Received Insert message: $message")
                        messageCache.addOrUpdate(
                            mapOf(message.chatId to mapOf(message.messageId to message))
                        )
                        MessageRealTimeEvent.Insert(message)
                    }
                    is PostgresAction.Update -> {
                        val message = Json.decodeFromString<MessageDTO>(data.record.toString()).toMessage()
                        Log(TAG, "Received UPDATE message: $message")
                        messageCache.update(
                            mapOf(message.chatId to mapOf(message.messageId to message))
                        )
                        MessageRealTimeEvent.Update(message)
                    } else -> {
                        throw IllegalStateException("Unknown action")
                    }
                }
            }
            .shareIn(coroutineScope, SharingStarted.Eagerly, 0)
    }




}
