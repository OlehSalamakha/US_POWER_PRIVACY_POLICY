package com.example.uspower.features.editprofile

import com.arkivanov.decompose.ComponentContext
import com.example.uspower.componentCoroutineScope
import com.example.uspower.core.login.LoginManager
import com.example.uspower.models.FileAbstraction
import com.example.uspower.models.Role
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProfileComponentImpl(
    componentContext: ComponentContext,
    private val loginManager: LoginManager,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val goBack: () -> Unit,
)
    : ComponentContext by componentContext, EditProfileComponent {

    val scope = componentCoroutineScope()
    override val firstName = MutableStateFlow(loginManager.user!!.firstName)
    override val lastName = MutableStateFlow(loginManager.user!!.lastName)
    override val aboutMe = MutableStateFlow(loginManager.user!!.description)


    override val role = MutableStateFlow(Role.fromString(loginManager.user!!.role))

    override val startDate = MutableStateFlow(loginManager.user!!.startDate)

    override val phoneNumber = MutableStateFlow(loginManager.user!!.phone)
    override val updateProfileProgress = MutableStateFlow(false)

    override val imageState: MutableStateFlow<FileAbstraction>
    =  MutableStateFlow(
        if (loginManager.user!!.photoUrl.isNotEmpty())
            FileAbstraction.Remote(loginManager.user!!.photoUrl)
        else FileAbstraction.None)


    override fun onFirstNameChanged(firstName: String) {
        this.firstName.value = firstName
    }

    override fun onLastNameChanged(lastName: String) {
        this.lastName.value = lastName
    }

    override fun onAboutChanged(about: String) {
        this.aboutMe.value = about
    }

    override fun onRoleChanged(role: Role) {
        this.role.value = role
    }

    override fun onStartDateChanged(date: String) {
        this.startDate.value = date
    }

    override fun onPhoneNumberChanged(phone: String) {
       this.phoneNumber.value = phone
    }

    override fun onImageLoaded(file: PlatformFile) {
        scope.launch {
            imageState.value = FileAbstraction.Local(file, file.readBytes())
        }
        println("100500 File path issss ${file.path}")
    }

    override fun onImageCropped(byteArray: ByteArray) {
        imageState.value = FileAbstraction.CroppedImage(byteArray)
    }

    override fun updateProfile() {

        val file = imageState.value
        val croppedImage = if ( file is FileAbstraction.CroppedImage) {
            file.byteArray
        } else null

        scope.launch {
            updateProfileProgress.value = true
            updateProfileUseCase.update(
                newImage = croppedImage,
                firstName = firstName.value,
                lastName = lastName.value,
                role = role.value,
                description = aboutMe.value,
                phone = phoneNumber.value,
                startDate = startDate.value)

            updateProfileProgress.value = false
        }

    }

    override fun onBackPressed() {
        goBack()
    }

    class Factory(
        private val loginManager: LoginManager,
        private val updateProfileUseCase: UpdateProfileUseCase,
    ): EditProfileComponent.Factory {
        override fun create(componentContext: ComponentContext, goBack: () -> Unit): EditProfileComponent {
            return EditProfileComponentImpl(componentContext, loginManager, updateProfileUseCase, goBack)
        }

    }
}