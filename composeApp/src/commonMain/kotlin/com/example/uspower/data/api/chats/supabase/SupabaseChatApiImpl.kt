package com.example.uspower.data.api.chats.supabase

import com.example.uspower.Log
import com.example.uspower.data.api.chats.supabase.entities.ChatDTO
import com.example.uspower.data.api.chats.supabase.entities.ChatViewDTO
import com.example.uspower.data.api.chats.supabase.entities.OwnersDTO
import com.example.uspower.data.api.chats.supabase.entities.ParticipantDTO
import com.example.uspower.data.api.chats.supabase.entities.UnreadCountDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class SupabaseChatApiImpl(
    private val client: SupabaseClient
): SupabaseChatApi {

    val postgrest = client.postgrest


    override suspend fun createChat(
        uuid: String,
        name: String,
        photoUrl: String,
        global: Boolean
    ): ChatDTO {
        return postgrest.from("Chats").insert(ChatDTO(uuid, photoUrl, global, name)) {
            select()
        }.decodeSingle<ChatDTO>()
    }

    override suspend fun setOwnerToChat(userid: String, chatId: String) {
        postgrest.from("Owners").insert(OwnersDTO(chatId, userid))
    }

    //list of user ids
    override suspend fun addMembersToChat(chatId: String, newMembers: List<String>) {
        postgrest.from("Participants").insert(
            newMembers.map {
                ParticipantDTO(chatId, it)
            }
        )
    }

    override suspend fun changeChatName(chatID: String, newName: String) {

        postgrest.from("Chats").update(
            {
                set("chatname", newName)
            },
            {
                filter {
                    eq("uuid", chatID)
                }
            }
        )
    }

    override suspend fun removeMembersFromChat(chatId: String, memberId: String) {
        println("100500, remove member with id $memberId from chat: ${chatId}")
        postgrest.from("Participants").delete {
            filter {
                ParticipantDTO::userid eq memberId
                ParticipantDTO::chatid eq chatId
            }
        }

    }

    override suspend fun removeChat(chatId: String) {
        postgrest.from("Chats").delete{
            filter {
                ChatDTO::uuid eq chatId
            }
        }
    }

    override suspend fun getChats(mail: String): List<ChatViewDTO> {
//        val jsonObject = JsonObject(
//            mapOf( "p_user_id" to JsonPrimitive(userId)
//        ))

        return postgrest.from("chat_overview").select().decodeList<ChatViewDTO>().filter { it.participants.contains(mail) }

    }

    @OptIn(SupabaseExperimental::class)
    override fun getChatFlow(chatId: String): Flow<ChatViewDTO> {
        return postgrest.from("chat_overview").selectSingleValueAsFlow(
            ChatViewDTO::chatid
        ) {
            eq("chatid", chatId)
        }
    }

    @OptIn(SupabaseExperimental::class)
    override fun getChatsFlow(mail: String): Flow<List<ChatViewDTO>> {
        return postgrest.from("chat_overview").selectAsFlow(
            ChatViewDTO::chatid
        ).map {
            it.filter { it.participants.contains(mail) }
        }
    }

    override suspend fun updateChatPhoto(chatId: String, photoUrl: String) {
        postgrest.from("Chats").update(
            {
                set("photourl", photoUrl)
            },
            {
                filter {
                    eq("uuid", chatId)
                }
            }
        )

    }

    override suspend fun updateLastReadAt(chatId: String, userId: String, timestamp: Instant) {

        postgrest.from("Participants").update(
            {
                set("last_read_at", timestamp)
            },
            {
                filter {
                    eq("chatid", chatId)
                    eq("userid", userId)
                }
            }
        )
    }

    override suspend fun getCountOfUnreadMessages(chatId: String, userid: String): Int {
        val jsonMap = mapOf(
            "user_uuid" to JsonPrimitive(userid),
            "chat_uuid" to JsonPrimitive(chatId)
        )
        val jsonObject = JsonObject(jsonMap)
        println("100500, update user: ${jsonObject}")


        val result = postgrest.rpc("count_unread_messages", jsonObject).decodeAs<Int>()
        return result
    }

    override suspend fun getCountOfUnreadMessagesForAllChats(userId: String): List<UnreadCountDto> {

        Log("SupabaseChatApi", "user id is $userId")
        val jsonMap = mapOf(
            "user_uuid" to JsonPrimitive(userId),
        )
        val jsonObject = JsonObject(jsonMap)

        val result = postgrest.rpc("get_unread_counts_for_all_my_chatss", jsonObject).decodeList<UnreadCountDto>()

        result.forEach {
            Log("SupabaseChatApi", "unreadcount for ${it.chatId} - ${it.unreadCount}")
        }
        return result


    }


}