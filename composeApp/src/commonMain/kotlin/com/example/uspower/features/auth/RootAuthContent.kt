package com.example.uspower.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.uspower.features.auth.login.LoginContent
import com.example.uspower.features.auth.signin.SignInContent
import com.example.uspower.features.auth.signup.SignUpContent


@Composable
fun RootAuthContent(
    component: AuthFlowComponent,
    modifier: Modifier
) {
    val childStack by component.childStack.collectAsState()


    Children(childStack, modifier = modifier, animation = stackAnimation(fade())) { child ->
        when(val instance = child.instance) {
            is AuthFlowComponent.Child.SignIn -> SignInContent(instance.signInComponent)
            is AuthFlowComponent.Child.Login -> LoginContent(instance.loginComponent)
            is AuthFlowComponent.Child.SignUp -> SignUpContent(instance.signUp)
        }
    }
}