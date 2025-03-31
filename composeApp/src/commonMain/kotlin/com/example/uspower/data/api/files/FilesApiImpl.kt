package com.example.uspower.data.api.files

import com.example.uspower.core.LoadState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.storage
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.Flow


class FilesApiImpl(
    private val uploader: PlatformSpecificUploader
) : FilesApi {
    override fun uploadFile(file: PlatformFile): Flow<LoadState<String>> {
        return uploader.uploadFile(file)
    }

    override fun downloadFile(url: String, fileName: String): Flow<LoadState<String>> {
       return uploader.downloadFile(url, fileName)
    }

    override fun uploadCroppedPhoto(byteArray: ByteArray): Flow<LoadState<String>> {
        return uploader.uploadCroppedPhoto(byteArray)
    }

    override suspend fun deleteFile(id: String) {
        Firebase.storage.reference.child(id).delete()
    }
}