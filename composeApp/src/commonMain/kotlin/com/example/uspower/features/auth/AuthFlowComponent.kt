package com.example.uspower.features.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.example.uspower.features.auth.login.LoginComponent
import com.example.uspower.features.auth.signin.SignInComponent
import com.example.uspower.features.auth.signup.SignUpComponent
import kotlinx.coroutines.flow.StateFlow

interface AuthFlowComponent {
    val childStack: StateFlow<ChildStack<*, Child>>

    sealed interface Child {
        class Login(val loginComponent: LoginComponent): Child
        class SignUp(val signUp: SignUpComponent): Child
        class SignIn(val signInComponent: SignInComponent): Child
    }

    interface Factory {
        fun create(componentContext: ComponentContext, onAuthFinished: () -> Unit): AuthFlowComponent
    }
}