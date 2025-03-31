package com.example.uspower.features.auth.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.users.UsersRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginComponentImpl(
    componentContext: ComponentContext,
    private val loginManager: LoginManager,
    private val onLogin: (userExists: Boolean) -> Unit,
): ComponentContext by componentContext, LoginComponent{
    override val mail = MutableStateFlow("")
    override val isLoad: MutableState<Boolean> = mutableStateOf(false)

    private val coroutineScope = componentCoroutineScope()

    init {
        coroutineScope.launch {
            mail.value = loginManager.readUserFromLocalPreferences()
        }
    }

    override fun onMailChanged(mail: String) {
        this.mail.value = mail
    }

    override fun onContinueClicked() {
        isLoad.value = true
        coroutineScope.launch {
            if (loginManager.login(mail.value) != null) {
                isLoad.value = false
                onLogin(true)
            } else {
                isLoad.value = false
                onLogin(false)
            }
        }
    }


    class Factory(
        private val loginManager: LoginManager
    ): LoginComponent.Factory {
        override fun create(componentContext: ComponentContext, onLogin: (userExists: Boolean) -> Unit): LoginComponent {
            return LoginComponentImpl(componentContext, loginManager, onLogin)
        }

    }
}