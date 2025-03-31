package com.example.uspower.models

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder


@Serializable
data class Chat(
    val chatId: String,
    val name: String,
    val photoUrl: String,
    val owners: MutableList<String>?,
    val participants: MutableList<String>?,
    val global: Boolean,
    val lastMessage: String?,
    @Serializable(with = TimestampSerializer::class)
    val lastMessageTimeStamp: Timestamp?,
    val lastSender: String?,
    val unreadCount: Int
)




object TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeLong(value.seconds) // Store as Unix timestamp (seconds)
    }

    override fun deserialize(decoder: Decoder): Timestamp {
        return Timestamp(decoder.decodeLong(), 0) // Create Timestamp from seconds
    }
}