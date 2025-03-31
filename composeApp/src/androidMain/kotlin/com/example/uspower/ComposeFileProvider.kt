package com.example.uspower

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.uspower.R
import java.io.File
import java.util.Objects

class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {

            val tempFile = File.createTempFile(
                "picture_${System.currentTimeMillis()}", ".png", context.cacheDir
            ).apply {
                createNewFile()
            }

            val authority = context.applicationContext.packageName + ".provider"

            println("getImageUri: ${tempFile.absolutePath}")
            return getUriForFile(
                Objects.requireNonNull(context),
                authority,
                tempFile,
            )
        }
    }
}