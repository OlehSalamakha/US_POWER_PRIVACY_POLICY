package com.example.uspower.features.mainflow.di

import com.example.uspower.features.mainflow.MainFlowComponent
import com.example.uspower.features.mainflow.MainFlowComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val mainFlowModule = module {
    single(createdAtStart = false) {
        MainFlowComponentImpl.Factory(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }.bind<MainFlowComponent.Factory>()

}
