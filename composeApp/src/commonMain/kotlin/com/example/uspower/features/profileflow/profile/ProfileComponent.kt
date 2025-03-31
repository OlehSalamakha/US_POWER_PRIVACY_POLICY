package com.example.uspower.features.profileflow.profile

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.models.User
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {
    val user: StateFlow<User>
    val showDialog: StateFlow<DialogState>

    fun onLogoutClicked()

    fun onLogoutConfirmed()

    fun onLogoutDialogDismissed()


    fun onDeleteProfileClicked()

    fun onDeleteProfileConfirmed()

    fun onDeleteDialogDismissed()


    fun onEditProfile()

    fun onCreateProfile()


    interface Factory {
        fun create(componentContext: ComponentContext, onLogout: () -> Unit, onEditProfileClicked: () -> Unit, onCreateProfile: () -> Unit): ProfileComponent
    }
}

data class DialogState(
    val show: Boolean,
    val title: String,
    val label: String,
    val confirmAction: () -> Unit,
    val dismissAction: () -> Unit
)