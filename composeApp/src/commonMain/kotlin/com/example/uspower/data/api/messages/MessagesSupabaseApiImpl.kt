package com.example.uspower.data.api.messages


import com.example.uspower.models.Message
import com.example.uspower.models.ReactionEmoji
import dev.gitlive.firebase.firestore.toMilliseconds
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


class MessagesSupabaseApiImpl(
    private val supabaseClient: SupabaseClient
): MessagesApi {
    private val postgrest = supabaseClient.postgrest
    override suspend fun getMessagesWithPagination(
        chatId: String,
        message: Message?
    ): List<Message> {
        val result = postgrest.from("Messages")
            .select() {
                order(column = "timestamp", order = Order.DESCENDING)
                filter {
                    eq("chatid", chatId)
                    message?.let {
                        lte("timestamp", Instant.fromEpochMilliseconds(message.timestamp.toMilliseconds().toLong() ))
                    }


                }
                limit(20)
            }.decodeList<MessageDTO>().map {

                it.reactions?.let {
                    it.forEach {
                        println("100500, sender is ${it.sender}, reaction is ${it.reaction}")
                    }
                }


                it.toMessage()
            }

        return result
    }

    override suspend fun sendMessage(chatId: String, message: Message) {
        val messageDTO = MessageDTO(
            message.messageId,
            Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds()),
            message.content,
            message.type,
            message.sender,
            chatId,
            null,
            isFirstMessageOfTheDay = false

        )

        supabaseClient.postgrest.from("Messages").insert(messageDTO)
    }

    override suspend fun addOrRemoveEmoji(
        chatId: String,
        emoji: ReactionEmoji,
        message: Message,
        sender: String
    ) {
        println("100500 add reaction sender is $sender")
        val messageFromSupabase = postgrest.from("Messages").select {
            filter {
                eq("uuid", message.messageId)
            }
        }.decodeSingle<MessageDTO>()


        val reactions = messageFromSupabase.reactions?.toMutableList() ?: mutableListOf()

        val existingReactionIndex = reactions.indexOfFirst { reaction ->
            reaction.sender == sender && reaction.reaction == emoji.code
        } ?: -1


        if (existingReactionIndex != -1) {
            reactions?.removeAt(existingReactionIndex)
        } else {
            reactions?.add(
                ReactionDTO(
                    sender,
                    emoji.code
                )
            )
        }
        println("100500 add reaction: ${reactions?.size} to message ${message.messageId}")
        postgrest.from("Messages").update( {
            set("reactions", reactions)

        }, {
            filter {
                eq("uuid", message.messageId)
            }
        })

    }

    override suspend fun removeMessage(chatId: String, messages: Message) {
        println("remoev message ${messages.messageId}")
//        postgrest.from("Messages").update(
//            {
//                set("deleted", true)
//            },
//            {
//            filter {
//                MessageDTO::uuid eq messages.messageId
//            }
//        })

        postgrest.from("Messages").delete{
            filter {
                MessageDTO::uuid eq messages.messageId
            }
        }
    }

}