package com.example.uspower.features.editprofile.di

import com.example.uspower.features.chat.chatlist.ChatListComponent
import com.example.uspower.features.editprofile.EditProfileComponent
import com.example.uspower.features.editprofile.EditProfileComponentImpl
import com.example.uspower.features.editprofile.UpdateProfileUseCase
import org.koin.dsl.bind
import org.koin.dsl.module

val editprofileModule = module {
    single <EditProfileComponent.Factory>{
        EditProfileComponentImpl.Factory(get(), get())
    }

    single <UpdateProfileUseCase>{
        UpdateProfileUseCase(get(), get(), get())
    }


}