package com.example.uspower.features.root.di


import com.example.uspower.features.root.RootFlowComponent
import com.example.uspower.features.root.RootFlowComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module


val rootModule = module {
    single {
        RootFlowComponentImpl.Factory(get(), get(), get())
    }.bind<RootFlowComponent.Factory>()


}
