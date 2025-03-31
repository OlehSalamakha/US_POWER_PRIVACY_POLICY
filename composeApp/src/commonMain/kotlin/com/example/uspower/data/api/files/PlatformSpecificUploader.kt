package com.example.uspower.data.api.files

import com.example.uspower.core.LoadState
import com.example.uspower.platformFileToData
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.FirebaseStorageMetadata
import dev.gitlive.firebase.storage.storage
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface PlatformSpecificUploader {
    fun uploadFile(file: PlatformFile): Flow<LoadState<String>>
    fun downloadFile(url: String, fileName: String): Flow<LoadState<String>>
    fun uploadCroppedPhoto(file: ByteArray, contentType: String = "image/jpeg"): Flow<LoadState<String>>
}



@OptIn(ExperimentalUuidApi::class)
fun commonUploadCroppedPhoto(file: ByteArray, contentType: String = "image/jpeg"): Flow<LoadState<String>> {
    return flow<LoadState<String>> {
        emit(LoadState.Loading)
        try {
            val uuid = Uuid.random().toString()
            val childImage = Firebase.storage.reference.child(uuid)
            println("100500 put fileee")
            childImage.putData(
                platformFileToData(file), metadata = FirebaseStorageMetadata(
                    contentType = contentType
                )
            )
            emit(LoadState.Success( childImage.getDownloadUrl()))

        } catch (exception: Exception) {
            emit(LoadState.Error(exception))
        }
    }
}

