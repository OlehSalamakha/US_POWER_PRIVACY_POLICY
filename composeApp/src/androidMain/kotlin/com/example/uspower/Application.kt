package com.example.uspower

import android.app.Application
import android.content.Context
import com.example.uspower.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.KoinAppDeclaration

class Application: Application() {

    companion object {
        lateinit var instance: Application
            private set

        val context: Context
            get() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initKoin {
            androidContext(this@Application)
        }
    }
}