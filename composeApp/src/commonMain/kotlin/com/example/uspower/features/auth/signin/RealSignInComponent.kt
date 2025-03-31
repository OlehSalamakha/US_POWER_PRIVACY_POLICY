package com.example.uspower.features.auth.signin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.LoginManager
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch

class RealSignInComponent(
    componentContext: ComponentContext,
    private val loginManager: LoginManager,
    private val onSignInSuccess: () -> Unit
): ComponentContext by componentContext, SignInComponent {
    override val isLoad: MutableState<Boolean> = mutableStateOf(false)
    override val mail = MutableStateFlow(loginManager.user?.email.orEmpty())
    override val password = MutableStateFlow("")
    override val rememberThisDevice = MutableStateFlow(false)

    private val coroutineScope = componentCoroutineScope()

    override fun onMailChanged(login: String) {
        mail.value = login
    }

    override fun onPasswordChanged(password: String) {
        this.password.value = password
    }

    override fun onRememberThisDeviceChanged(newValue: Boolean) {
        rememberThisDevice.value = newValue
    }

    override suspend fun onLoginClick() {
        isLoad.value = true
        val user = loginManager.signIn(mail.value, password.value)

        if (user != null) {
            if (rememberThisDevice.value) {
                loginManager.saveUserToLocalPreferences(mail.value)
            }
            isLoad.value = false
            onSignInSuccess()
        } else {
            isLoad.value = false
            println("failed")
        }

    }

    class Factory(
        private val loginManager: LoginManager
    ) : SignInComponent.Factory {
        override fun create(componentContext: ComponentContext, onSignInSuccess: () -> Unit): SignInComponent {
            return RealSignInComponent(componentContext, loginManager, onSignInSuccess)
        }

    }
}