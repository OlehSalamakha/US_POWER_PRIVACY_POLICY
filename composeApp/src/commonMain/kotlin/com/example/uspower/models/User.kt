package com.example.uspower.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uuid: String = "",
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val photoUrl: String = "",
    val role: String = "",
    val description: String = "",
    val phone: String = "",
    val approved: Boolean = false,
    val rememberMe: Boolean = false,
    val admin: Boolean = false,
    val startDate: String = "",
    var fcmToken: String = ""
)