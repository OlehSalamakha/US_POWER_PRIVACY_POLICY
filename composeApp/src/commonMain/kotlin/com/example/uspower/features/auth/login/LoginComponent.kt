package com.example.uspower.features.auth.login

import androidx.compose.runtime.MutableState
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface LoginComponent {
    val mail: StateFlow<String>
    val isLoad: MutableState<Boolean>

    fun onMailChanged(mail: String)

    fun onContinueClicked()



    interface Factory {
        fun create(componentContext: ComponentContext, onLogin: (userExists: Boolean) -> Unit): LoginComponent
    }
}