package com.example.uspower.features.singleuser


import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.LoginManager
import com.example.uspower.core.users.UsersRepository
import com.example.uspower.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SingleUserComponentImpl(
    componentContext: ComponentContext,
    private val usersRepository: UsersRepository,
    private val loginManager: LoginManager,
    private val selectedUser: User,
    private val goBack: () -> Unit
): ComponentContext by componentContext, SingleUserComponent {

    val scope = componentCoroutineScope()

    init {
        scope.launch(Dispatchers.IO) {
            usersRepository.getUserFlow(selectedUser).collectLatest {
                println("100500 collect, show approve button: ${loginManager.user?.admin == true && !it.approved}, all user is ${it}")
                state.value = SingleUserComponentState(it, loginManager.user?.admin == true, loginManager.user?.admin == true && !it.approved && it.email.isNotBlank())
            }
        }
    }
    override val state = MutableStateFlow(SingleUserComponentState(selectedUser, loginManager.user?.admin == true, loginManager.user?.admin == true && !selectedUser.approved,))


    override fun approveUser() {
        scope.launch(Dispatchers.IO) {
            usersRepository.approveUser(state.value.user)
        }

    }

    override fun deleteUserClicked() {

        state.value = state.value.copy(showConfirmationDialog = true)
    }

    override fun deleteDialogDismissed() {
        state.value = state.value.copy(showConfirmationDialog = false)
    }

    override fun deleteUserConfirmed() {
        scope.launch (Dispatchers.IO) {
            usersRepository.deleteUser(state.value.user)
            state.value = state.value.copy(showConfirmationDialog = false)
            goBack()
        }
    }

    override fun onGoBackClicked() {
        goBack()
    }


    class Factory(
        val usersRepository: UsersRepository,
        val loginManager: LoginManager
        ): SingleUserComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            user: User,
            goBack: () -> Unit
        ): SingleUserComponent {
            return SingleUserComponentImpl(componentContext, usersRepository, loginManager, user, goBack)
        }

    }

}