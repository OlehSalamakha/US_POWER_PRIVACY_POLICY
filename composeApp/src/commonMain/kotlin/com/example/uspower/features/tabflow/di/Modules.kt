package com.example.uspower.features.tabflow.di

import com.example.uspower.features.tabflow.TabFlowComponent
import com.example.uspower.features.tabflow.TabFlowComponentImpl
import org.koin.dsl.bind
import org.koin.dsl.module

val tabModule = module {
    single<TabFlowComponent.Factory> {
        TabFlowComponentImpl.Factory(get(), get(), get(), get())
    }.bind<TabFlowComponent.Factory>()


}
