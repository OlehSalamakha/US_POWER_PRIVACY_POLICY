package com.example.uspower

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import com.example.uspower.di.initKoin
import com.example.uspower.features.root.RootFlowComponent
import com.example.uspower.permissions.createPermissionControllerWrapper
import dev.icerock.moko.permissions.ios.PermissionsController
import org.koin.mp.KoinPlatform

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin()
}) {

    val permissioncontroller = createPermissionControllerWrapper()

    val rootComponent = remember {
        val rootFactory: RootFlowComponent.Factory = KoinPlatform.getKoin().get()

        rootFactory.create(DefaultComponentContext(ApplicationLifecycle()), permissioncontroller)
    }
    App(rootComponent)

}