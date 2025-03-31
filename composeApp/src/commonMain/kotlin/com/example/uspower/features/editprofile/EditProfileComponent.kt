package com.example.uspower.features.editprofile

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.core.LoadState
import com.example.uspower.models.FileAbstraction
import com.example.uspower.models.Role
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.StateFlow

interface EditProfileComponent {
    val firstName: StateFlow<String>

    val lastName: StateFlow<String>

    val aboutMe: StateFlow<String>

    val role: StateFlow<Role>

    val startDate: StateFlow<String>

    val phoneNumber: StateFlow<String>

    val imageState: StateFlow<FileAbstraction>

    val updateProfileProgress: StateFlow<Boolean>




    fun onFirstNameChanged(firstName: String)

    fun onLastNameChanged(lastName: String)

    fun onAboutChanged(about: String)

    fun onRoleChanged(role: Role)

    fun onStartDateChanged(date: String)

    fun onPhoneNumberChanged(phone: String)

    fun onImageLoaded(file: PlatformFile)

    fun onImageCropped(byteArray: ByteArray)


    fun updateProfile()

    fun onBackPressed()

    interface Factory {
        fun create(
            componentContext: ComponentContext,
            goBack: () -> Unit
        ): EditProfileComponent
    }
}