package com.example.uspower.features.root

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.base.FlowComponent
import com.example.uspower.features.auth.AuthFlowComponent
import com.example.uspower.features.mainflow.MainFlowComponent
import com.example.uspower.features.splash.SplashComponent
import com.example.uspower.permissions.PermissionControllerWrapper



interface RootFlowComponent: FlowComponent<RootFlowComponent.Child>{

    sealed interface Child {
        class Splash(val splashComponent: SplashComponent): Child
        class Auth(val authFlowComponent: AuthFlowComponent): Child
        class MainFlow(val mainFlowComponent: MainFlowComponent): Child

    }

    interface Factory {
        fun create(componentContext: ComponentContext, permissionsController: PermissionControllerWrapper): RootFlowComponent
    }
}