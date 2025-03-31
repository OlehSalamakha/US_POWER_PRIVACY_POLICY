package com.example.uspower.features.auth.signup

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.LoginManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SignUpComponentImpl(
    componentContext: ComponentContext,
    private val loginManager: LoginManager,
    private val onSignUpFinished: () -> Unit
): ComponentContext by componentContext, SignUpComponent {
    override val firstName = MutableStateFlow("")
    override val lastName = MutableStateFlow("")
    override val mailAddress = MutableStateFlow("")
    override val confirmMailAddress = MutableStateFlow("")

    override val password = MutableStateFlow("")

    private val coroutineScope = componentCoroutineScope()

    override fun onFirstNameChanged(firstName: String) {
        this.firstName.value = firstName
    }

    override fun onLastNameChange(lastName: String) {
        this.lastName.value = lastName
    }

    override fun onMailAddressChanged(mail: String) {
        this.mailAddress.value = mail
    }

    override fun onConfirmationMailAddressChanged(mail: String) {
        this.confirmMailAddress.value = mail
    }

    override fun onPasswordChanged(password: String) {
        this.password.value = password
    }

    override fun onContinueClicked() {
        coroutineScope.launch {
            val user = loginManager.signUp(
                firstName = firstName.value,
                lastName = lastName.value,
                mail = mailAddress.value,
                confirmationMail = confirmMailAddress.value,
                password = password.value
            )

            if (user != null) {
                println("New user created, signup success")
                onSignUpFinished()
            } else {
                println("Error during signup")
            }
        }
    }

    class Factory(
        private val loginManager: LoginManager
    ): SignUpComponent.Factory {
        override fun create(
            componentContext: ComponentContext,
            onSignupFinished: () -> Unit
        ): SignUpComponent {
            return SignUpComponentImpl(componentContext, loginManager, onSignupFinished)
        }

    }
}