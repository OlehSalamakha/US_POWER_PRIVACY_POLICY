package com.example.uspower.features.tabflow

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.base.FlowComponent
import com.example.uspower.features.chat.chatlist.ChatListComponent
import com.example.uspower.features.profileflow.profile.ProfileComponent

import com.example.uspower.features.userlist.UsersListComponent
import com.example.uspower.models.Chat
import com.example.uspower.models.User

interface TabFlowComponent: FlowComponent<TabFlowComponent.Child> {

    fun onChatTabClicked()
    fun onUsersTabClicked()
    fun onProfileTabClicked()

    fun onEditProfileClicked()

    fun onLogout()

    sealed interface Child {
        class Chat(val chatFlowComponent: ChatListComponent): Child
        class Users(val usersListComponent: UsersListComponent): Child
        class Profile(val profileComponent: ProfileComponent): Child
    }

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onChatSelected: (chat: Chat) -> Unit,
            onUserSelected: (user: User) -> Unit,
            onLogout: () -> Unit,
            onEditProfile: () -> Unit,
            createNewChat: () -> Unit,
            createProfile: () -> Unit
        ): TabFlowComponent
    }


}