package com.example.uspower.features.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.push
import com.example.uspower.base.FlowComponentImpl
import com.example.uspower.features.auth.AuthFlowComponent
import com.example.uspower.features.mainflow.MainFlowComponent
import com.example.uspower.features.splash.SplashComponent
import com.example.uspower.permissions.PermissionControllerWrapper
import kotlinx.serialization.KSerializer

class RootFlowComponentImpl(
    componentContext: ComponentContext,
    private val authFlowComponentFactory: AuthFlowComponent.Factory,
    private val splashComponentFactory: SplashComponent.Factory,
    private val mainFlowComponentFactory: MainFlowComponent.Factory,
    private val permissionsController: PermissionControllerWrapper,
): ComponentContext by componentContext, RootFlowComponent,
    FlowComponentImpl<RootFlowComponent.Child, RootFlowComponentImpl.Config>() {


    override val initialConfiguration: Config = Config.Splash
    override val configSerializer: KSerializer<Config> = Config.serializer()
    override val handleBack: Boolean = false


    fun onAuthFinished() {
        println("100500 on auth finisehd")
        nav.push    (
            configuration =  Config.MainFlow
        )
    }

    fun onSplashFinished(signInSuccess: Boolean) {
        if (signInSuccess) {
            println("100500 onSplashFinished success")
            onAuthFinished()
        } else {
            nav.push(
                configuration = Config.Auth
            )
        }
    }


    fun onLogout() {
        println("10050000 logout")
       nav.popTo(0)
    }

    override fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootFlowComponent.Child {
        return when(config) {
            Config.Auth -> RootFlowComponent.Child.Auth(
                authFlowComponentFactory.create(componentContext, ::onAuthFinished)
            )

            Config.MainFlow -> RootFlowComponent.Child.MainFlow(
                mainFlowComponentFactory.create(componentContext, permissionsController, ::onLogout)
            )

            Config.Splash -> RootFlowComponent.Child.Splash(
                splashComponentFactory.create(componentContext, ::onSplashFinished)
            )

        }
    }


    @kotlinx.serialization.Serializable
    sealed interface Config {
        @kotlinx.serialization.Serializable
        data object Splash : Config

        @kotlinx.serialization.Serializable
        data object Auth : Config

        @kotlinx.serialization.Serializable
        data object MainFlow : Config
    }

    class Factory(
        private val authFactory: AuthFlowComponent.Factory,
        private val splashFactory: SplashComponent.Factory,
        private val mainFlowComponentFactory: MainFlowComponent.Factory,
    ): RootFlowComponent.Factory {
        override fun create(componentContext: ComponentContext, permissionsController: PermissionControllerWrapper): RootFlowComponent {
            println("100500 create root flow")
            return RootFlowComponentImpl(componentContext, authFactory, splashFactory, mainFlowComponentFactory, permissionsController)
        }

    }

}