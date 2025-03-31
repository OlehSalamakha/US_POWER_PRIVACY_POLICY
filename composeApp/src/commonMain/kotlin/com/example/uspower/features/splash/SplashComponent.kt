package com.example.uspower.features.splash

import com.arkivanov.decompose.ComponentContext

interface SplashComponent {

    fun processSplashScreen()

    interface Factory {
        fun create(componentContext: ComponentContext, onSplashFinished: (signInSuccess: Boolean) -> Unit): SplashComponent
    }

}