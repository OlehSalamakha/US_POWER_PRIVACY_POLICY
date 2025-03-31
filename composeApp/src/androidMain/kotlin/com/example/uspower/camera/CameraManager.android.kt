package com.example.uspower.camera

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.example.uspower.ComposeFileProvider
import java.io.InputStream

@Composable
actual fun rememberCameraManager(onResult: (ImageBitmap?) -> Unit): CameraManager {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    var tempPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(

        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onResult.invoke(BitmapUtils.getBitmapFromUri(tempPhotoUri, contentResolver))
            }
        }
    )
    return remember {
        CameraManager(
            onLaunch = {
                tempPhotoUri = ComposeFileProvider.getImageUri(context)
                cameraLauncher.launch(tempPhotoUri)
            }
        )
    }
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class CameraManager actual constructor(
    private val onLaunch: () -> Unit
) {
    actual fun launch() {
        onLaunch()
    }
}


object BitmapUtils {
    fun getBitmapFromUri(uri: Uri, contentResolver: ContentResolver): ImageBitmap? {
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(uri)
            val s = BitmapFactory.decodeStream(inputStream)

            inputStream?.close()
            return s.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            println("getBitmapFromUri Exception: ${e.message}")
            println("getBitmapFromUri Exception: ${e.localizedMessage}")
            return null
        }
    }
}