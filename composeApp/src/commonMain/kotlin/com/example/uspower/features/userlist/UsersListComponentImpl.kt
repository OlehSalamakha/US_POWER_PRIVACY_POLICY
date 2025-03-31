package com.example.uspower.features.userlist

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class UsersListComponentImpl(
    componentContext: ComponentContext,
    private val usersRepository: UsersRepository,
    private val loginManager: LoginManager,
    private val onUserSelected: (user: User) -> Unit
): ComponentContext by componentContext, UsersListComponent {

    private val coroutineScope = componentCoroutineScope()
    override val userSearch = MutableStateFlow("")


    override val users = combine(userSearch, usersRepository.usersFLow.map {
        it.filter { user ->
            if (loginManager.user?.email != user.email) {
                println("100500, curent user admin: ${loginManager.user?.admin == true}")
                loginManager.user?.admin == true || user.approved
            } else {
                false
            }
        }.map {
            UIUser(
                it,
                !it.approved && loginManager.user?.admin == true
            )
        }

    })

    { searchQuery, userList ->
        if (searchQuery.isBlank()) {
            userList
        } else {
            userList.filter { uiUser ->
                uiUser.user.firstName.contains(searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    override fun onSearchChanged(newSearch: String) {
        userSearch.value = newSearch
    }

    override fun onUserClicked(uiUser: UIUser) {
        onUserSelected(uiUser.user)
    }

    override fun onApproveClicked(user: UIUser) {
        coroutineScope.launch(Dispatchers.IO) {
            usersRepository.approveUser(user.user)
        }

    }


    class Factory(
        private val usersRepository: UsersRepository,
        private val loginManager: LoginManager,
    ): UsersListComponent.Factory {
        override fun create(componentContext: ComponentContext, onUserSelected: (user: User) -> Unit): UsersListComponent {
            return UsersListComponentImpl(componentContext, usersRepository, loginManager, onUserSelected)
        }
    }


}