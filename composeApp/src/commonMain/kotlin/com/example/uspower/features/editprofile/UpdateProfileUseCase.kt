package com.example.uspower.features.editprofile

import com.example.uspower.core.login.LoginManager
import com.example.uspower.data.api.files.FilesApi
import com.example.uspower.core.LoadState
import com.example.uspower.data.api.users.UsersApi
import com.example.uspower.models.Role
import com.example.uspower.models.User


class UpdateProfileUseCase(
    private val loginManager: LoginManager,
    private val filesApi: FilesApi,
    private val usersApi: UsersApi,
) {

    suspend fun update(
        newImage: ByteArray? = null,
        firstName: String,
        lastName: String,
        role: Role,
        description: String,
        phone: String,
        startDate: String
    ) {


        val user = loginManager.user!!

        newImage?.let {
            filesApi.uploadCroppedPhoto(it).collect { uploadResult ->
                when (uploadResult) {
                    is LoadState.Error -> {

                    }
                    LoadState.Loading -> {}
                    is LoadState.Success -> {
                        val imageUrl = uploadResult.data
                        updateInternal(user,firstName, lastName, role, description, phone, startDate, imageUrl)
                        println("100500, image url is $imageUrl")

                    }
                }
            }
        } ?: run {
            updateInternal(user, firstName, lastName, role, description, phone, startDate)
        }
    }


    private suspend fun updateInternal(user: User,
                       firstName: String,
                       lastName: String,
                       role: Role,
                       description: String,
                       phone: String,
                       startDate: String,
                                       imageUrl: String? = null
                                       ) {

        val userMap = hashMapOf(
            "firstname" to firstName,
            "last_name" to lastName,
            "position" to role.customValue,
//                            "description" to description,
            "phone" to phone,
            "start_date" to startDate
        )

        if (imageUrl != null) {
            userMap.put("photo_url", imageUrl)
        }

        println("100500, UpdateProfileUse casee call api")
        usersApi.updateUser(user, userMap)

        val updatedUser = usersApi.getUserByEmail(user.email)
        updatedUser?.let {
            loginManager.user = it
        }
    }
}

