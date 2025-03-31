package com.example.uspower.features.singleuser.di

import com.example.uspower.features.singleuser.SingleUserComponent
import com.example.uspower.features.singleuser.SingleUserComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val singleUserModule = module {
    single<SingleUserComponent.Factory> {
        SingleUserComponentImpl.Factory(
            get(),
            get()
        )
    }.bind<SingleUserComponent.Factory>()
}