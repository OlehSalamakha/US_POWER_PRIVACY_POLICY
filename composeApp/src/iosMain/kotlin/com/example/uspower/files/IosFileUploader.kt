package com.example.uspower.files

import com.example.uspower.data.api.files.CommonUploader
import com.example.uspower.core.LoadState
import com.example.uspower.data.api.files.PlatformSpecificUploader
import com.example.uspower.data.api.files.commonUploadCroppedPhoto
import io.github.vinceglb.filekit.core.PlatformFile
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.dataWithBytes
import platform.Foundation.writeToFile

class IosFileUploader(
    private val commonUploader: CommonUploader
): PlatformSpecificUploader {
    override fun uploadFile(file: PlatformFile): Flow<LoadState<String>> {
        return commonUploader.uploadFile(file)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun downloadFile(url: String, fileName: String): Flow<LoadState<String>> {
        return flow {
            emit(LoadState.Loading)
            try {
                val extension = fileName.substringAfterLast('.', "").lowercase()
                val tempDir = NSTemporaryDirectory()
                val fullPath = "$tempDir$fileName.$extension"

                val fileManager = NSFileManager.defaultManager
                if (!fileManager.fileExistsAtPath(fullPath)) {
                    fileManager.createFileAtPath(fullPath, null, null)
                }
                val response: HttpResponse = HttpClient().get(url)
                val data = response.readBytes()
//            val saveFile = FileKit.saveFile(extension = extension, bytes = data)
                val nsData = data.usePinned { pinned ->
                    NSData.dataWithBytes(pinned.addressOf(0), data.size.toULong())
                }
                nsData.writeToFile(fullPath, true)
                emit(LoadState.Success(fullPath))

            } catch (exception: Exception) {
                exception.printStackTrace()
                emit(LoadState.Error(exception))
            }
        }
    }

    override fun uploadCroppedPhoto(file: ByteArray, contentType: String): Flow<LoadState<String>> {
        return commonUploadCroppedPhoto(file, contentType)
    }
}