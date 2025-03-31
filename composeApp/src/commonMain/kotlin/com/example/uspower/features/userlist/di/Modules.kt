package com.example.uspower.features.userlist.di


import com.example.uspower.features.userlist.UsersListComponent
import com.example.uspower.features.userlist.UsersListComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val usersModule = module {
    single<UsersListComponent.Factory> {
        UsersListComponentImpl.Factory(
            get(),
            get()

        )
    }.bind<UsersListComponent.Factory>()
}