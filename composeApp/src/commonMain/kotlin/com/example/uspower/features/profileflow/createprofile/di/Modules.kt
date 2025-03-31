package com.example.uspower.features.profileflow.createprofile.di


import com.example.uspower.features.profileflow.createprofile.CreateProfileComponent
import com.example.uspower.features.profileflow.createprofile.CreateProfileComponentImpl
import org.koin.dsl.module


val createProfileModule = module {
    single<CreateProfileComponent.Factory> {
        CreateProfileComponentImpl.Factory(
            get()

        )
    }
}

//
//val profileModule = module {
//    single<ProfileComponent.Factory> {
//        ProfileComponentImpl.Factory(
//            loginManager = get(),
//        )
//    }.bind<ProfileComponent.Factory>()
//}