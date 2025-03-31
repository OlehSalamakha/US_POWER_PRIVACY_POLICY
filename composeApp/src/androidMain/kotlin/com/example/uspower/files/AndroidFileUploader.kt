package com.example.uspower.files

import com.example.uspower.Application.Companion.context
import com.example.uspower.data.api.files.CommonUploader
import com.example.uspower.core.LoadState
import com.example.uspower.data.api.files.PlatformSpecificUploader
import com.example.uspower.data.api.files.commonUploadCroppedPhoto
import io.github.vinceglb.filekit.core.PlatformFile
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class AndroidFileUploader(
    private val commonUploader: CommonUploader
): PlatformSpecificUploader {
    override fun uploadFile(file: PlatformFile): Flow<LoadState<String>> {
        return commonUploader.uploadFile(file)
    }

    override fun downloadFile(url: String, fileName: String): Flow<LoadState<String>> {
        return flow<LoadState<String>> {
            try {
                emit(LoadState.Loading)
                val response: HttpResponse = HttpClient().get(url)
                val tempFile = File.createTempFile("download_", ".tmp", context.cacheDir)

                tempFile.writeBytes(response.readBytes())

                println("100500 download file")
                emit(LoadState.Success(tempFile.absolutePath))

            } catch (exception: Exception) {
                println("exception is ${exception}")
                emit(LoadState.Error(exception))
            }
        }
    }

    override fun uploadCroppedPhoto(file: ByteArray, contentType: String): Flow<LoadState<String>> {
        return commonUploadCroppedPhoto(file, contentType)
    }
}