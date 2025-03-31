package com.example.uspower

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import dev.gitlive.firebase.storage.Data
import io.github.vinceglb.filekit.core.FileKit
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.refTo
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSString
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import org.jetbrains.skia.Image
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.EncodedImageFormat


import platform.Foundation.dataWithBytes
import platform.Foundation.pathExtension
import platform.Foundation.writeToFile
import platform.UIKit.UIApplication
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIPasteboard
import platform.UIKit.UIViewController
import platform.UIKit.endEditing
import platform.UniformTypeIdentifiers.UTType
import platform.darwin.NSObject

actual val needToCrop: Boolean = true
actual val shouldShowBackButton = true

@OptIn(ExperimentalForeignApi::class)
actual fun platformFileToData(file: ByteArray): Data {
    val t = file.usePinned { NSData.create(it.addressOf(0), file.size.convert()) }
    return Data(t)

}

private var documentController: UIDocumentInteractionController? = null

class DocumentInteractionDelegate : NSObject(), UIDocumentInteractionControllerDelegateProtocol {
    override fun documentInteractionControllerViewControllerForPreview(controller: UIDocumentInteractionController): UIViewController =
        UIApplication.sharedApplication.keyWindow?.rootViewController!!
}

actual fun openFile(filePath: String, fileName: String) {
    val url = NSURL.fileURLWithPath(filePath)
    documentController = UIDocumentInteractionController()
    documentController?.let { documentController ->
        documentController.delegate = DocumentInteractionDelegate()
        documentController.setURL(url)
        documentController.presentPreviewAnimated(true)
    }
}


actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(
        NotificationPlatformConfiguration.Ios(
            showPushNotification = false,
            askNotificationPermissionOnStart = true,
//            notificationSoundName = "custom_notification_sound.wav"
        )
    )
}

actual fun ImageBitmap.toByteArray(format: ImageFormat): ByteArray {
    val width = this.width
    val height = this.height

    val pixelMap = this.toPixelMap()
    val intPixels = pixelMap.buffer

    println("100500 has alpha ${this.hasAlpha}, config is ${this.config}")


    val byteArray = ByteArray(intPixels.size * 4)
    intPixels.forEachIndexed { index, pixel ->
        val baseIndex = index * 4


        val A = (pixel shr 24) and 0xff
        val R = (pixel shr 16) and 0xff
        val G = (pixel shr 8) and 0xff
        val B = pixel and 0xff


        byteArray[baseIndex+ 3] = A.toByte() // A
        byteArray[baseIndex+ 2] = R.toByte() // R
        byteArray[baseIndex+ 1] = G.toByte() // G
        byteArray[baseIndex +0] = B.toByte() // B
    }

    val skiaBitmap = Bitmap().apply {
        allocN32Pixels(width, height)
        installPixels(byteArray)
    }

    val skiaImage = Image.makeFromBitmap(skiaBitmap)
    val encodedFormat = when (format) {
        ImageFormat.PNG -> EncodedImageFormat.PNG
        ImageFormat.JPEG -> EncodedImageFormat.JPEG
    }
    return skiaImage.encodeToData(encodedFormat)?.bytes ?: ByteArray(0)
}


actual fun getMimeType(path: String): String {
    val extension = (path as NSString).pathExtension
    return UTType.filenameExtensionToMimeType(extension) ?: ""
}

private fun UTType.Companion.filenameExtensionToMimeType(extension: String): String? {
    val utType = UTType.typeWithFilenameExtension(extension) ?: return null
    return utType.preferredMIMEType
}

actual fun isMainThread(): Boolean {
   return false
}

actual fun hideKeyboard() {

//    UIApplication.sharedApplication.sendAction(
//        selector = NSSelectorFromString("resignFirstResponder"),
//        to = null,
//        from = null,
//        forEvent = null
//    )
    UIApplication.sharedApplication.keyWindow?.endEditing(true)
}

actual fun copyToClipboard(text: String) {
    UIPasteboard.generalPasteboard.string = text
}