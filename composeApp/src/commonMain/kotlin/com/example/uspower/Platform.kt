package com.example.uspower

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform