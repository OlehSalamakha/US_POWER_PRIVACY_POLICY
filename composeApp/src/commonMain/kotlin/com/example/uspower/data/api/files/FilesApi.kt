package com.example.uspower.data.api.files

import com.example.uspower.core.LoadState
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.Flow

interface FilesApi {
    fun uploadFile(file: PlatformFile): Flow<LoadState<String>>
    fun downloadFile(url: String, fileName: String): Flow<LoadState<String>>
    fun uploadCroppedPhoto(byteArray: ByteArray): Flow<LoadState<String>>
    suspend fun deleteFile(id: String)

}