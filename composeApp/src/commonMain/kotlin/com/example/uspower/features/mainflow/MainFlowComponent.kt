package com.example.uspower.features.mainflow

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.base.FlowComponent
import com.example.uspower.features.chat.chatroom.ChatRoomComponent
import com.example.uspower.features.chat.editchat.EditChatComponent
import com.example.uspower.features.chat.editchat.addmembers.AddMembersRootComponent
import com.example.uspower.features.createchat.NewChatFlowComponent
import com.example.uspower.features.editprofile.EditProfileComponent
import com.example.uspower.features.profileflow.createprofile.CreateProfileComponent
import com.example.uspower.features.tabflow.TabFlowComponent
import com.example.uspower.features.singleuser.SingleUserComponent
import com.example.uspower.permissions.PermissionControllerWrapper


interface MainFlowComponent: FlowComponent<MainFlowComponent.Child> {
    sealed interface Child {
        class TabFlow(val tabFlowComponent: TabFlowComponent): Child
        class ChatRoom(val chatRoomComponent: ChatRoomComponent): Child
        class EditProfile(val editProfileComponent: EditProfileComponent): Child
        class UserScreen(val singleUserComponent: SingleUserComponent): Child
        class CreateChatFlow(val newChatFlowComponent: NewChatFlowComponent): Child
        class EditChat(val component: EditChatComponent): Child
        class AddMembersToExistingChat(val component: AddMembersRootComponent): Child
        class CreateProfile(val component: CreateProfileComponent): Child
    }

    interface Factory {
        fun create(componentContext: ComponentContext, permissionsController: PermissionControllerWrapper, onLogout: () -> Unit): MainFlowComponent
    }
}