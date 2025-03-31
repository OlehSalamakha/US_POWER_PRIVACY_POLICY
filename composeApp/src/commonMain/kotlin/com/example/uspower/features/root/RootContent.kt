package com.example.uspower.features.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.uspower.features.auth.RootAuthContent
import com.example.uspower.features.mainflow.MainFlowContent
import com.example.uspower.features.splash.SplashScreen
import kotlinx.coroutines.Dispatchers

@Composable
fun RootContent(
    component: RootFlowComponent,
    modifier: Modifier
) {
    print("100500, Start root content")
    val childStack by component.childStack.collectAsState(Dispatchers.Main.immediate)

    Children(childStack, modifier = modifier, animation = stackAnimation(fade())) { child ->
        when(val instance = child.instance) {
            is RootFlowComponent.Child.Auth -> RootAuthContent(instance.authFlowComponent, modifier)
            is RootFlowComponent.Child.MainFlow -> MainFlowContent(instance.mainFlowComponent, modifier)
            is RootFlowComponent.Child.Splash -> SplashScreen(instance.splashComponent, modifier)
        }
    }
}