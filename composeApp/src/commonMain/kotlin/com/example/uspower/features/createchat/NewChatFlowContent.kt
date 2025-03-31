package com.example.uspower.features.createchat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.uspower.features.createchat.addandcreate.AddAndCreateChatContent
import com.example.uspower.features.createchat.chatname.ChatNameContent
import kotlinx.coroutines.Dispatchers


@Composable
fun NewChatFlowContent(
    component: NewChatFlowComponent,
    modifier: Modifier
) {
    val childStack by component.childStack.collectAsState(Dispatchers.Main.immediate)

    Children(childStack, modifier = modifier, animation = stackAnimation(fade())) { child ->
        when(val instance = child.instance) {
            is NewChatFlowComponent.Child.AddAndCreate -> AddAndCreateChatContent(instance.component, modifier = modifier)
            is NewChatFlowComponent.Child.ChatName -> ChatNameContent(instance.component, modifier = modifier)
        }
    }
}