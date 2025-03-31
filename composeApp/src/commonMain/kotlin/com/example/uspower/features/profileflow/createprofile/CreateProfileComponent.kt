package com.example.uspower.features.profileflow.createprofile

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow


interface CreateProfileComponent {

    val state: StateFlow<CreateProfileState>

    fun backClicked()

    fun changeFirstName(name: String)

    fun changeLastName(lastName: String)

    fun changeEmail(mail: String)

    fun changeConfirmationMail(mail: String)

    fun changePassword(password: String)

    fun changeConfirmationPassword(password: String)


    fun createProfile()

    interface Factory {
        fun create(componentContext: ComponentContext, goBack: () -> Unit): CreateProfileComponent
    }
}


data class CreateProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val mail: String = "",
    val confirmationMail: String = "",
    val password: String = "",
    val confirmationPassword: String = "",
    val showLoading: Boolean = false,
    val createButtonEnabled: Boolean = false
)