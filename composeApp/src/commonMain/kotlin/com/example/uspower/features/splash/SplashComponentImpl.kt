package com.example.uspower.features.splash

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.LoginManager
import kotlinx.coroutines.launch

class SplashComponentImpl(
    componentContext: ComponentContext,
    private val loginManager: LoginManager,
    private val onSplashFinished: (signInSuccess: Boolean) -> Unit
): ComponentContext by componentContext, SplashComponent {

    private val coroutineScope = componentCoroutineScope()

    override fun processSplashScreen() {
        coroutineScope.launch {
            val mail = loginManager.readUserFromLocalPreferences()
            val user = if (mail.isNotEmpty()) {
                loginManager.login(mail)
            } else {
                null
            }

            onSplashFinished(user != null)
        }

    }



    class Factory(private val loginManager: LoginManager): SplashComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onSplashFinished: (signInSuccess: Boolean) -> Unit
        ): SplashComponent {
            return SplashComponentImpl(componentContext, loginManager, onSplashFinished)
        }

    }
}