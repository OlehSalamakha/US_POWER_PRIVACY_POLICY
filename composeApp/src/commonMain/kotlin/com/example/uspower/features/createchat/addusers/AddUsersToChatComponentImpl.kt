package com.example.uspower.features.createchat.addusers

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AddUsersToChatComponentImpl(
    componentContext: ComponentContext,
    private val usersRepository: UsersRepository,
    private val loginManager: LoginManager,
    private val filter: (user: User) -> Boolean = { true }
): ComponentContext by componentContext, AddUsersToChatComponent {


    private val coroutineScope = componentCoroutineScope()
    override val userSearch = MutableStateFlow("")

    override val selectedUsers = MutableStateFlow(emptySet<User>())


    override fun onSearchChanged(newSearch: String) {
        userSearch.value = newSearch
    }

    override fun onUserClicked(user: User) {
        if (selectedUsers.value.contains(user)) {
            selectedUsers.value -= user
        } else {
            println("100500 add to selection")
            selectedUsers.value += user
        }
    }


    override val users = combine(userSearch, usersRepository.usersFLow.map {
        it.filter { user ->

            if (loginManager.user?.email != user.email) {
                (loginManager.user?.admin == true || user.approved) && filter(user)
            } else {
                false
            }
        }

    }) { searchQuery, userList ->
        if (searchQuery.isBlank()) {
            userList
        } else {
            userList.filter { user ->
                user.firstName.contains(searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )


    class Factory(
        private val usersRepository: UsersRepository,
        private val loginManager: LoginManager,
    ): AddUsersToChatComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            userFilter: (user: User) -> Boolean

        ): AddUsersToChatComponent {
            return AddUsersToChatComponentImpl(componentContext, usersRepository, loginManager, userFilter)
        }

    }

}