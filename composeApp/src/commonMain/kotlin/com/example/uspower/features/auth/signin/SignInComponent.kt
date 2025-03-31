package com.example.uspower.features.auth.signin


import androidx.compose.runtime.MutableState
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface SignInComponent {
    val isLoad: MutableState<Boolean>

    val mail: StateFlow<String>
    val password: StateFlow<String>

    val rememberThisDevice: StateFlow<Boolean>

    fun onMailChanged(login: String)

    fun onPasswordChanged(password: String)

    fun onRememberThisDeviceChanged(newValue: Boolean)

    suspend fun onLoginClick()

    interface Factory {
        fun create(componentContext: ComponentContext, onSignInSuccess: () -> Unit): SignInComponent

    }

}