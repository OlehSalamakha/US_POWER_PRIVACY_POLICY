package com.example.uspower.features.auth

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.example.uspower.features.auth.login.LoginComponent
import com.example.uspower.features.auth.signin.SignInComponent
import com.example.uspower.features.auth.signup.SignUpComponent
import com.example.uspower.toStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthFlowComponentImpl(
    componentContext: ComponentContext,
    private val loginComponentFactory: LoginComponent.Factory,
    private val signInComponentFactory: SignInComponent.Factory,
    private val signUpComponentFactory: SignUpComponent.Factory,
    private val onAuthDone: () -> Unit

): ComponentContext by componentContext, AuthFlowComponent {
    private val nav = StackNavigation<Config>()

    private  fun onLogin(userExists: Boolean) {
        if (userExists) {
            nav.push(
                configuration = AuthFlowComponentImpl.Config.SignIn
            )
        } else {
            nav.push(
                configuration = Config.SignUp
            )
        }
    }

    private fun onAuthFinished() {
        this.onAuthDone()
    }

    override val childStack: StateFlow<ChildStack<*, AuthFlowComponent.Child>> = childStack(
        source = nav,
        serializer = Config.serializer(),
        initialConfiguration = Config.Login,
        handleBackButton = true,
        childFactory = ::child
    ).toStateFlow(lifecycle)



    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): AuthFlowComponent.Child {
        return when(config) {
            Config.Login -> AuthFlowComponent.Child.Login(
                loginComponentFactory.create(componentContext, ::onLogin)
            )

            Config.SignIn -> AuthFlowComponent.Child.SignIn(
                signInComponentFactory.create(componentContext, ::onAuthFinished)
            )

            Config.SignUp -> AuthFlowComponent.Child.SignUp(
                signUpComponentFactory.create(componentContext, ::onAuthFinished)
            )
        }
    }





    @kotlinx.serialization.Serializable
    private sealed interface Config {
        @kotlinx.serialization.Serializable
        data object Login : Config
        @kotlinx.serialization.Serializable
        data object SignIn : Config

        @kotlinx.serialization.Serializable
        data object SignUp : Config


    }

    class Factory(
        private val loginComponentFactory: LoginComponent.Factory,
        private val signInComponentFactory: SignInComponent.Factory,
        private val signUpComponentFactory: SignUpComponent.Factory,
    ): AuthFlowComponent.Factory {
        override fun create(componentContext: ComponentContext, onAuthFinished: () -> Unit): AuthFlowComponent {
            return AuthFlowComponentImpl(
                componentContext,
                loginComponentFactory,
                signInComponentFactory,
                signUpComponentFactory,
                onAuthFinished
                )
        }

    }

}