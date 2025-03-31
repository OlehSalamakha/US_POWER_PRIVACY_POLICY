package com.example.uspower.features.profileflow.createprofile

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.encode
import com.example.uspower.core.users.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateProfileComponentImpl(
    componentContext: ComponentContext,
    private val usersRepository: UsersRepository,
    private val goBack: () -> Unit,
): ComponentContext by componentContext, CreateProfileComponent {
    override val state = MutableStateFlow(CreateProfileState())

    val scope = componentCoroutineScope()
    override fun backClicked() {
        goBack()
    }

    override fun changeFirstName(name: String) {
        state.value = state.value.copy(firstName = name)
        enableInviteButton()
    }

    override fun changeLastName(lastName: String) {
       state.value = state.value.copy(lastName = lastName)
        enableInviteButton()
    }

    override fun changeEmail(mail: String) {
        state.value = state.value.copy(mail = mail)
        enableInviteButton()
    }

    override fun changeConfirmationMail(mail: String) {
        state.value = state.value.copy(confirmationMail = mail)
        enableInviteButton()
    }

    override fun changePassword(password: String) {
        state.value = state.value.copy(password = password)
        enableInviteButton()
    }

    override fun changeConfirmationPassword(password: String) {
        state.value = state.value.copy(confirmationPassword = password)
        enableInviteButton()
    }

    override fun createProfile() {
        scope.launch(Dispatchers.IO) {
            state.value = state.value.copy(showLoading = true)
            usersRepository.createUser(
                firstName = state.value.firstName,
                lastName = state.value.lastName,
                mail = state.value.mail,
                password = encode(state.value.password),
                approved = true
            )
            state.value = state.value.copy(showLoading = false)
            goBack()
        }
    }

    private fun enableInviteButton() {

        if (state.value.firstName.isNotEmpty()
            && state.value.lastName.isNotEmpty() && state.value.mail.isNotEmpty() && state.value.confirmationMail.isNotEmpty()
            && state.value.password.isNotEmpty() && state.value.confirmationPassword.isNotEmpty() && state.value.mail == state.value.confirmationMail
            && state.value.password == state.value.confirmationPassword) {
            state.value = state.value.copy(createButtonEnabled = true)
        } else {
            state.value = state.value.copy(createButtonEnabled = false)
        }
    }


    class Factory(
        private val usersRepository: UsersRepository
    ): CreateProfileComponent.Factory {
        override fun create(componentContext: ComponentContext, goBack: () -> Unit): CreateProfileComponent {
            return CreateProfileComponentImpl(componentContext, usersRepository, goBack)
        }

    }


}