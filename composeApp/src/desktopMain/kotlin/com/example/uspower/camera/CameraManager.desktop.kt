package com.example.uspower.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
actual fun rememberCameraManager(onResult: (ImageBitmap?) -> Unit): CameraManager {
    return CameraManager({})
}

actual class CameraManager actual constructor(onLaunch: () -> Unit) {
    actual fun launch() {
    }
}