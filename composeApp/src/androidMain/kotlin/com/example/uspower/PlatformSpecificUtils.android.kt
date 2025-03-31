package com.example.uspower

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.os.Looper
import android.webkit.MimeTypeMap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.uspower.Application.Companion.context
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.uspower.R
import dev.gitlive.firebase.storage.Data
import java.io.ByteArrayOutputStream
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

actual val needToCrop: Boolean = true
actual val shouldShowBackButton = true
actual fun platformFileToData(file: ByteArray): Data {
    return Data(file)
}

@SuppressLint("QueryPermissionsNeeded")
actual fun openFile(filePath: String, fileName: String) {
    val extension = fileName.substringAfterLast('.', "").lowercase() ?: "*/*"

    val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
    println("100500, mime type from extension: $mime")

    val pdfFile = File(filePath)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", pdfFile)


    println("100500 uri is ${uri.path}")

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mime)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    val t = Intent.createChooser(intent, "Open PDF with").apply {
        addFlags(FLAG_ACTIVITY_NEW_TASK)
    }
        context.startActivity(t)

}



actual fun onApplicationStartPlatformSpecific() {
//    val customNotificationSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + "com.mmk.kmpnotifier.sample" + "/" + R.raw.custom_notification_sound)
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = R.mipmap.ic_launcher_foreground,
            showPushNotification = false,

//            notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
////                soundUri = customNotificationSound.toString()
//            )
        )
    )
}

actual fun ImageBitmap.toByteArray(format: ImageFormat): ByteArray {
    val width = this.width
    val height = this.height

    // Extract pixels from ImageBitmap as ARGB IntArray
    val pixelMap = this.toPixelMap()
    val intPixels = pixelMap.buffer

    // Convert ARGB IntArray to ByteArray (RGBA format)
    val byteArray = ByteArray(intPixels.size * 4)
    intPixels.forEachIndexed { index, pixel ->
        val baseIndex = index * 4
        byteArray[baseIndex] = ((pixel shr 16) and 0xFF).toByte() // R
        byteArray[baseIndex + 1] = ((pixel shr 8) and 0xFF).toByte() // G
        byteArray[baseIndex + 2] = (pixel and 0xFF).toByte() // B
        byteArray[baseIndex + 3] = ((pixel shr 24) and 0xFF).toByte() // A
    }

    // Convert ByteArray to Android Bitmap
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.copyPixelsFromBuffer(java.nio.ByteBuffer.wrap(byteArray))

    // Convert Bitmap to ByteArray using Android's compress method
    val outputStream = ByteArrayOutputStream()
    val compressedFormat = when (format) {
        ImageFormat.PNG -> Bitmap.CompressFormat.PNG
        ImageFormat.JPEG -> Bitmap.CompressFormat.JPEG
    }
    bitmap.compress(compressedFormat, 100, outputStream)
    return outputStream.toByteArray()
}


actual fun getMimeType(path: String): String {
    val extension = MimeTypeMap.getFileExtensionFromUrl(path)
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: ""
}

actual fun isMainThread(): Boolean {
    return Looper.getMainLooper().thread == Thread.currentThread()
}

actual fun hideKeyboard() {
}

actual fun copyToClipboard(text: String) {
    val context = context
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}