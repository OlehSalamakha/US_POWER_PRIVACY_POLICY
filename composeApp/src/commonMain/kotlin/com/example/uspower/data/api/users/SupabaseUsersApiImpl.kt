package com.example.uspower.data.api.users



import com.example.uspower.core.login.encode
import com.example.uspower.models.User
import com.example.uspower.models.dto.SupabaseUser
import com.example.uspower.models.dto.toFirebaseUser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.realtime.selectSingleValueAsFlow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SupabaseUsersApiImpl(
    private val client: SupabaseClient
): UsersApi {
    private val postgrest = client.postgrest
    override suspend fun getUsers(): List<User> {
        val result = postgrest.from("Users").select()
        return result.decodeList<SupabaseUser>().map { it.toFirebaseUser() }
    }

    @OptIn(SupabaseExperimental::class)
    override fun getUsersFlow(): Flow<List<User>> {
        return postgrest.from("Users").selectAsFlow(SupabaseUser::uuid).map {
            it.map { it.toFirebaseUser() }
        }
    }

    @OptIn(SupabaseExperimental::class)
    override fun getSingleUserFlow(user: User): Flow<User> {
        return postgrest
            .from("Users")
            .selectSingleValueAsFlow(SupabaseUser::uuid) {
                SupabaseUser::email eq user.email
            }.map {
                it.toFirebaseUser()
            }
    }

    override suspend fun getUserByEmail(mail: String): User? {
        println("100500 get user by email: $mail")
        return postgrest.from("Users").select {
            filter {
                eq("email", mail)
            }
            single()


        }.decodeAs<SupabaseUser>().toFirebaseUser()

    }

    override suspend fun getUserById(id: String): User? {
        println("100500 get user by email: $id")
        return postgrest.from("Users").select {
            filter {
                eq("uuid", id)
            }
            single()


        }.decodeAs<SupabaseUser>().toFirebaseUser()
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createUser(
        firstName: String,
        lastName: String,
        mail: String,
        password: String,
        approved: Boolean
    ): User? {


        val user = SupabaseUser(
            Uuid.random().toString(),
            firstName,
            lastName,
            null,
            null,
            true,
            false,
            mail,
            null,
            null,
            encode(password)
        )

        val result = postgrest.from("Users").insert(user) { select() }

         return    result.decodeSingle<SupabaseUser>().toFirebaseUser()
    }

    override suspend fun deleteUser(user: User) {
        postgrest.from("Users").delete {
           filter {
               User::email eq user.email
           }
        }
    }

    override suspend fun updateUser(user: User, map: Map<String, Any>) {

        val jsonMap = map.mapValues { JsonPrimitive(it.value.toString()) }
        val jsonObject = JsonObject(jsonMap)
        println("100500, update user: ${jsonObject}")
//
//        val response = supabase.from("users")
//            .update(jsonObject)
//            .eq("id", userId) // Ensure the correct user is updated
        postgrest.from("Users").update(jsonObject, {
            filter {
                SupabaseUser::email eq user.email
            }
        })
    }


}