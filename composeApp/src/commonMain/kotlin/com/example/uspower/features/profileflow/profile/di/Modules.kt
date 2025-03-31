package com.example.uspower.features.profileflow.profile.di


import com.example.uspower.features.profileflow.profile.DeleteProfileUseCase
import com.example.uspower.features.profileflow.profile.ProfileComponent
import com.example.uspower.features.profileflow.profile.ProfileComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val profileModule = module {
    single<ProfileComponent.Factory> {
        ProfileComponentImpl.Factory(
            loginManager = get(),
            notificationSubscriber = get(),
            chatRepository = get()
        )
    }.bind<ProfileComponent.Factory>()

    single<DeleteProfileUseCase> {
        DeleteProfileUseCase(get(), get())
    }
}

