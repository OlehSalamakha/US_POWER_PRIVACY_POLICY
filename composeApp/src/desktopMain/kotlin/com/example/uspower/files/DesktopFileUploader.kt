package com.example.uspower.files

import com.example.uspower.data.api.files.FirebaseUploadFileResponse
import com.example.uspower.core.LoadState
import com.example.uspower.data.api.files.PlatformSpecificUploader
import com.example.uspower.getMimeType
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PlatformFile
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DesktopFileUploader(
    private val httpClient: HttpClient
): PlatformSpecificUploader {
    private val baseUrl ="https://firebasestorage.googleapis.com/v0/b/us-power-app.appspot.com/o?uploadType=media"


    @OptIn(ExperimentalUuidApi::class)
    override fun uploadFile(file: PlatformFile): Flow<LoadState<String>> {
        return flow<LoadState<String>> {
            emit(LoadState.Loading)
            try {
                val fileName = Uuid.random().toString()
                val requestUrl = URLBuilder(baseUrl).apply {
                    parameters.append("name", fileName)
                }.buildString()


                val response: HttpResponse = httpClient.post(requestUrl) {
                    headers[HttpHeaders.ContentType] = getMimeType(file.path ?: "")
                    setBody(file.readBytes())
                }

                val serializedResponse = response.body<FirebaseUploadFileResponse>()
                val url = "https://firebasestorage.googleapis.com/v0/b/${serializedResponse.bucket}/o/${serializedResponse.name.replace("/", "%2F")}?alt=media"
                emit(LoadState.Success(url))

            } catch (e: Exception) {
                println("Error uploading file: ${e.message}")
                emit(LoadState.Error(e))
            } finally {
                httpClient.close()
            }
        }
    }

    override fun downloadFile(url: String, fileName: String): Flow<LoadState<String>> {
        return flow<LoadState<String>> {
            emit(LoadState.Loading)
            try {
                val extension = fileName.substringAfterLast('.', "").lowercase() ?: "*/*"
                val response: HttpResponse = HttpClient().get(url)
                val data = response.readBytes()
                val saveFile = FileKit.saveFile(extension = extension, bytes = data)

                emit(LoadState.Success(saveFile?.path.orEmpty()))

            } catch (exception: Exception) {
               emit(LoadState.Error(exception))
            }
        }
    }



    @OptIn(ExperimentalUuidApi::class)
    override fun uploadCroppedPhoto(file: ByteArray, contentType: String): Flow<LoadState<String>> {
        return flow<LoadState<String>> {
            emit(LoadState.Loading)
            try {
                val fileName = Uuid.random().toString()
                val requestUrl = URLBuilder(baseUrl).apply {
                    parameters.append("name", fileName)
                }.buildString()


                val response: HttpResponse = httpClient.post(requestUrl) {
                    headers[HttpHeaders.ContentType] = contentType
                    setBody(file)
                }

                val serializedResponse = response.body<FirebaseUploadFileResponse>()
                val url = "https://firebasestorage.googleapis.com/v0/b/${serializedResponse.bucket}/o/${serializedResponse.name.replace("/", "%2F")}?alt=media"
                emit(LoadState.Success(url))

            } catch (e: Exception) {
                println("Error uploading file: ${e.message}")
                emit(LoadState.Error(e))
            } finally {
                httpClient.close()
            }
        }
    }
}