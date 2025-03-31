package com.example.uspower.features.auth.signup

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow

interface SignUpComponent {
    val firstName: StateFlow<String>

    val lastName: StateFlow<String>

    val mailAddress: StateFlow<String>

    val confirmMailAddress: StateFlow<String>

    val password: StateFlow<String>

    fun onFirstNameChanged(firstName: String)

    fun onLastNameChange(lastName: String)

    fun onMailAddressChanged(mail: String)

    fun onConfirmationMailAddressChanged(mail: String)

    fun onPasswordChanged(password: String)

    fun onContinueClicked()


    interface Factory {
        fun create(
            componentContext: ComponentContext,
            onSignupFinished: () -> Unit
        ): SignUpComponent
    }
}