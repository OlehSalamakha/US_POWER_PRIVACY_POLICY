package com.example.uspower.data.api.files

import kotlinx.serialization.Serializable


@Serializable
data class FirebaseUploadFileResponse(
    val name: String,
    val bucket: String,
    val contentType: String,
)