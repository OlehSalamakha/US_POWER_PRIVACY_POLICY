package com.example.uspower.features.singleuser

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.models.User
import kotlinx.coroutines.flow.StateFlow

interface SingleUserComponent {
    val state: StateFlow<SingleUserComponentState>

    fun approveUser()

    fun deleteUserClicked()
    fun deleteDialogDismissed()

    fun deleteUserConfirmed()

    fun onGoBackClicked()
    interface Factory {
        fun create(componentContext: ComponentContext, user: User, goBack: () -> Unit): SingleUserComponent
    }
}


data class SingleUserComponentState(
    val user: User,
    val showDeleteButton: Boolean,
    val showApproveButton: Boolean,
    val showConfirmationDialog: Boolean = false
)