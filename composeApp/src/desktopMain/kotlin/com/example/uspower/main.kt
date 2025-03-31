package com.example.uspower

import android.app.Application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.example.uspower.di.initKoin
import com.example.uspower.features.root.RootFlowComponent
import com.example.uspower.permissions.PermissionControllerWrapper
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import org.koin.java.KoinJavaComponent.inject
import javax.swing.SwingUtilities

fun main() {
    initKoin()

    FirebasePlatform.initializeFirebasePlatform(object : FirebasePlatform() {
        val storage = mutableMapOf<String, String>()
        override fun store(key: String, value: String) = storage.set(key, value)
        override fun retrieve(key: String) = storage[key]
        override fun clear(key: String) { storage.remove(key) }
        override fun log(msg: String) = println(msg)
    })

    val options: FirebaseOptions =  FirebaseOptions(
        apiKey = "AIzaSyA9D4BhaGAW6rlH-pqlGftAjB2leMS3p6I",
        applicationId = "1:104692665240:android:68e48a2fa3dc4a342479ed",
        projectId = "us-power-app"
    )

    val app = Firebase.initialize( Application(), options)

    val windowState = WindowState()
    val lifecycle = LifecycleRegistry()
    val rootFactory: RootFlowComponent.Factory by inject(RootFlowComponent.Factory::class.java)

    val rootComponent = runOnMainThreadBlocking {
        rootFactory.create(
            componentContext = DefaultComponentContext(lifecycle),
            object: PermissionControllerWrapper {
                override suspend fun providePermission() {

                }

            }
        )
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "US Power",
        ) {
            LifecycleController(lifecycle, windowState)
            App(rootComponent)
        }
    }
}


private inline fun <T : Any> runOnMainThreadBlocking(crossinline block: () -> T): T {
    lateinit var result: T
    SwingUtilities.invokeAndWait { result = block() }
    return result
}