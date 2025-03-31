package com.example.uspower.features.createchat.addusers
import kotlinx.coroutines.flow.StateFlow
import com.arkivanov.decompose.ComponentContext
import com.example.uspower.models.User

interface AddUsersToChatComponent {

    val users: StateFlow<List<User>>
    val selectedUsers: StateFlow<Set<User>>
    val userSearch: StateFlow<String>


    fun onSearchChanged(newSearch: String)

    fun onUserClicked(user: User)

    interface Factory {
        fun create(componentContext: ComponentContext, userFilter: (user: User) -> Boolean = {true}): AddUsersToChatComponent
    }
}