package com.example.uspower

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toPixelMap
import dev.gitlive.firebase.storage.Data
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import io.github.vinceglb.filekit.core.FileKit
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.Locale

actual val needToCrop: Boolean = false
actual val shouldShowBackButton = true

//
//actual fun platformFileToData(file: ByteArray): Data {
////    val t = file.usePinned { NSData.create(it.addressOf(0), file.size.convert()) }
////    return Data(t)
//
//    Da
//
//}
actual fun platformFileToData(file: ByteArray): Data {
    return Data()
}

actual fun openFile(filePath: String, fileName: String) {
    val file = File(filePath)
    if (file.exists()) {
        Desktop.getDesktop().open(file)
    } else {
        println("File not found")
    }
}



actual fun onApplicationStartPlatformSpecific() {
}


//
//actual fun ImageBitmap.toByteArray(format: ImageFormat): ByteArray {
//    TODO("Not yet implemented")
//}
actual fun ImageBitmap.toByteArray(format: ImageFormat): ByteArray {
    val width = this.width
    val height = this.height

    val pixelMap = this.toPixelMap()
    val intPixels = pixelMap.buffer

    // Convert ARGB IntArray to ByteArray (RGBA format)
    val byteArray = ByteArray(intPixels.size * 4)
    intPixels.forEachIndexed { index, pixel ->
        val baseIndex = index * 4

        val A = (pixel shr 24) and 0xff
        val R = (pixel shr 16) and 0xff
        val G = (pixel shr 8) and 0xff
        val B = pixel and 0xff


        byteArray[baseIndex+ 3] = A.toByte()
        byteArray[baseIndex+ 2] = R.toByte()
        byteArray[baseIndex+ 1] = G.toByte()
        byteArray[baseIndex +0] = B.toByte()
    }

    // Create a Skia Bitmap and load pixels
    val skiaBitmap = Bitmap().apply {
        allocN32Pixels(width, height)
        installPixels(byteArray)
    }

    // Convert Skia Bitmap to Image and encode
    val skiaImage = Image.makeFromBitmap(skiaBitmap)
    val encodedFormat = when (format) {
        ImageFormat.PNG -> EncodedImageFormat.PNG
        ImageFormat.JPEG -> EncodedImageFormat.JPEG
    }
    return skiaImage.encodeToData(encodedFormat)?.bytes ?: ByteArray(0)
}


actual fun getMimeType(path: String): String {
    val filePath = Paths.get(path)

    // Try to determine MIME type using Files.probeContentType
    val mimeType = Files.probeContentType(filePath)
    if (mimeType != null) return mimeType

    // Fallback: Use extension mapping
    val extension = path.substringAfterLast('.', "").lowercase(Locale.getDefault())
    return mimeTypeMap[extension] ?: "application/octet-stream" // Default binary type
}

// Fallback MIME type mapping
private val mimeTypeMap = mapOf(
    "txt" to "text/plain",
    "html" to "text/html",
    "htm" to "text/html",
    "css" to "text/css",
    "js" to "application/javascript",
    "json" to "application/json",
    "xml" to "application/xml",
    "jpg" to "image/jpeg",
    "jpeg" to "image/jpeg",
    "png" to "image/png",
    "gif" to "image/gif",
    "bmp" to "image/bmp",
    "webp" to "image/webp",
    "mp3" to "audio/mpeg",
    "wav" to "audio/wav",
    "mp4" to "video/mp4",
    "avi" to "video/x-msvideo",
    "mov" to "video/quicktime",
    "pdf" to "application/pdf",
    "zip" to "application/zip",
    "rar" to "application/vnd.rar",
    "tar" to "application/x-tar",
    "gz" to "application/gzip"
)

actual fun isMainThread(): Boolean {
   return false
}

actual fun hideKeyboard() {
}

actual fun copyToClipboard(text: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val selection = StringSelection(text)
    clipboard.setContents(selection, null)
}