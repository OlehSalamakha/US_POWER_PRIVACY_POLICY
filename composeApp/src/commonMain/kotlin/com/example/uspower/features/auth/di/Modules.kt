package com.example.uspower.features.auth.di

import com.example.uspower.features.auth.AuthFlowComponent
import com.example.uspower.features.auth.AuthFlowComponentImpl
import com.example.uspower.features.auth.login.LoginComponent
import com.example.uspower.features.auth.login.LoginComponentImpl
import com.example.uspower.features.auth.signin.RealSignInComponent
import com.example.uspower.features.auth.signin.SignInComponent
import com.example.uspower.features.auth.signup.SignUpComponent
import com.example.uspower.features.auth.signup.SignUpComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val authModule = module {
    single<SignInComponent.Factory> {
        RealSignInComponent.Factory(
            loginManager = get(),
        )
    }.bind<SignInComponent.Factory>()

    single<SignUpComponent.Factory> {
        SignUpComponentImpl.Factory(
            loginManager = get(),
        )
    }.bind<SignUpComponent.Factory>()

    single<LoginComponent.Factory> {
        LoginComponentImpl.Factory(
            loginManager = get(),
        )
    }.bind<LoginComponent.Factory>()


    single<AuthFlowComponent.Factory> {
        AuthFlowComponentImpl.Factory(
            signInComponentFactory = get(),
            loginComponentFactory = get(),
            signUpComponentFactory = get()
        )
    }.bind<AuthFlowComponent.Factory>()
}