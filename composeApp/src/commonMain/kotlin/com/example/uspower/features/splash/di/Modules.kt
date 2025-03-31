package com.example.uspower.features.splash.di


import com.example.uspower.features.splash.SplashComponent
import com.example.uspower.features.splash.SplashComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val splashModule = module {
    single<SplashComponent.Factory> {
        SplashComponentImpl.Factory(
            get(),
        )
    }.bind<SplashComponent.Factory>()
}