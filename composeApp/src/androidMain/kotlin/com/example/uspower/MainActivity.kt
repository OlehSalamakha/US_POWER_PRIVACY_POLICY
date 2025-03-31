package com.example.uspower

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.arkivanov.decompose.defaultComponentContext
import com.example.uspower.features.root.RootFlowComponent
import com.example.uspower.permissions.CommonPermissionControllerWrapper
import com.mmk.kmpnotifier.permission.permissionUtil
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.vinceglb.filekit.core.FileKit
import org.koin.android.ext.android.inject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionUtil by permissionUtil()
        permissionUtil.askNotificationPermission()


        AppInitializer.onApplicationStart()

        val rootFactory: RootFlowComponent.Factory by inject()

        FileKit.init(this)



        setContent {
            val factory = rememberPermissionsControllerFactory()
            val controller = remember(factory) {
                factory.createPermissionsController()
            }

            BindEffect(controller)
            SetStatusBarColor(Color(0xFF000069), darkIcons = false)

            App(component = rootFactory.create(defaultComponentContext(), CommonPermissionControllerWrapper(controller)))
        }
    }

    override fun onStart() {
        super.onStart()
        val ns: String = Context.NOTIFICATION_SERVICE
        val nMgr = getSystemService(ns) as NotificationManager
        nMgr.cancelAll()
    }
}

@Composable
fun SetStatusBarColor(color: Color, darkIcons: Boolean) {
    val activity = LocalContext.current as ComponentActivity
    val window: Window = activity.window
    val view = LocalView.current

    SideEffect {
        window.statusBarColor = color.toArgb()
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = darkIcons
    }
}
