package com.example.uspower.features.tabflow

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.uspower.features.chat.chatlist.ChatListContent
import com.example.uspower.features.profileflow.profile.ProfileScreen
import com.example.uspower.features.userlist.UsersScreen
import com.example.uspower.ui.navigationBarColors
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.vectorResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_chat
import uspower.composeapp.generated.resources.ic_employee
import uspower.composeapp.generated.resources.ic_profile


@Composable
fun TabFlowContent(
    tabFlowComponent: TabFlowComponent,
    modifier: Modifier = Modifier
    ) {

    Column(modifier = modifier) {
        ChildScreens(component = tabFlowComponent, modifier = Modifier.weight(1F).consumeWindowInsets(WindowInsets.navigationBars))
        BottomBar(component = tabFlowComponent, modifier = Modifier.fillMaxWidth())
    }

}

@Composable
private fun ChildScreens(component: TabFlowComponent, modifier: Modifier = Modifier) {
    val stack by component.childStack.collectAsState(Dispatchers.Main.immediate)
    Children(
        stack = stack,
        modifier = modifier,
        animation = stackAnimation(slide())
    ) {
        when(val instance = it.instance) {
            is TabFlowComponent.Child.Chat -> ChatListContent(instance.chatFlowComponent, Modifier.fillMaxSize())
            is TabFlowComponent.Child.Users -> UsersScreen(instance.usersListComponent, Modifier.fillMaxSize())
            is TabFlowComponent.Child.Profile -> ProfileScreen(instance.profileComponent, Modifier.fillMaxSize())
        }
    }
}



@Composable
private fun BottomBar(
    component: TabFlowComponent,
    modifier: Modifier = Modifier
) {
    val stack by component.childStack.collectAsState(Dispatchers.Main.immediate)

    val activeComponent = stack.active.instance

    BottomAppBar (
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
            .fillMaxWidth()
    ) {
        NavigationBarItem(
            selected = activeComponent is TabFlowComponent.Child.Chat,
            onClick = component::onChatTabClicked,
            colors = navigationBarColors,
            icon = {
                Icon(
                    imageVector = vectorResource(resource = Res.drawable.ic_chat),
                    contentDescription = "Chat",
                )
            },
        )

        NavigationBarItem(
            selected = activeComponent is TabFlowComponent.Child.Users,
            onClick = component::onUsersTabClicked,
            colors = navigationBarColors,
            icon = {
                Icon(
                    imageVector = vectorResource(resource = Res.drawable.ic_employee),
                    contentDescription = "Users",
                )
            },
        )

        NavigationBarItem(
            selected = activeComponent is TabFlowComponent.Child.Profile,
            onClick = component::onProfileTabClicked,
            colors = navigationBarColors,
            icon = {
                Icon(
                    imageVector = vectorResource(resource = Res.drawable.ic_profile),
                    contentDescription = "Profile",
                )
            },
        )
    }
}