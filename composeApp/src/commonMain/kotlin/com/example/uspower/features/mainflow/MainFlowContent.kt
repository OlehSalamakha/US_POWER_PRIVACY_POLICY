package com.example.uspower.features.mainflow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.example.uspower.features.chat.chatroom.ChatRoomScreen
import com.example.uspower.features.chat.editchat.EditChatContent
import com.example.uspower.features.chat.editchat.addmembers.AddMembersRootContent
import com.example.uspower.features.createchat.NewChatFlowContent
import com.example.uspower.features.editprofile.EditProfileContent
import com.example.uspower.features.profileflow.createprofile.CreateProfileContent
import com.example.uspower.features.tabflow.TabFlowContent
import com.example.uspower.features.singleuser.UserContent
import kotlinx.coroutines.Dispatchers

@Composable
fun MainFlowContent(
    component: MainFlowComponent,
    modifier: Modifier
) {
    val childStack by component.childStack.collectAsState(Dispatchers.Main.immediate)

    Children(
        childStack,
        modifier = modifier,
        animation = stackAnimation(fade())
    ) { child ->
        when(val instance = child.instance) {
            is MainFlowComponent.Child.TabFlow -> TabFlowContent(instance.tabFlowComponent)
            is MainFlowComponent.Child.ChatRoom -> ChatRoomScreen(instance.chatRoomComponent, modifier)
            is MainFlowComponent.Child.UserScreen -> UserContent(instance.singleUserComponent, modifier)
            is MainFlowComponent.Child.EditProfile -> EditProfileContent(instance.editProfileComponent)
            is MainFlowComponent.Child.CreateChatFlow -> NewChatFlowContent(instance.newChatFlowComponent, modifier)
            is MainFlowComponent.Child.EditChat -> EditChatContent(instance.component, modifier)
            is MainFlowComponent.Child.AddMembersToExistingChat -> AddMembersRootContent(instance.component, modifier)
            is MainFlowComponent.Child.CreateProfile -> CreateProfileContent(instance.component, modifier)
        }
    }
}