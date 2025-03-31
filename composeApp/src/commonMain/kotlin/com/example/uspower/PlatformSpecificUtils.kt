package com.example.uspower

import androidx.compose.ui.graphics.ImageBitmap
import dev.gitlive.firebase.storage.Data
import kotlinx.coroutines.flow.Flow


expect val needToCrop: Boolean

expect val shouldShowBackButton: Boolean



expect fun platformFileToData(file: ByteArray): Data
expect fun openFile(filePath: String, fileName: String)



expect fun onApplicationStartPlatformSpecific(): Unit


expect fun ImageBitmap.toByteArray(format: ImageFormat = ImageFormat.JPEG): ByteArray

enum class ImageFormat {
    PNG, JPEG
}



expect fun getMimeType(path: String): String


expect fun isMainThread(): Boolean

expect fun hideKeyboard()

expect fun copyToClipboard(text: String)