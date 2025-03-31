package com.example.uspower.features.userlist

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.models.User
import kotlinx.coroutines.flow.StateFlow


interface UsersListComponent {
    val users: StateFlow<List<UIUser>>
    val userSearch: StateFlow<String>

    fun onSearchChanged(newSearch: String)

    fun onUserClicked(user: UIUser)

    fun onApproveClicked(user: UIUser)

    interface Factory {
        fun create(componentContext: ComponentContext, onUserSelected: (user: User) -> Unit): UsersListComponent
    }
}

data class UIUser(
    val user: User,
    val showApproveButton: Boolean
)