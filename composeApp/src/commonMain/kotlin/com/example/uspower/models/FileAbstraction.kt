package com.example.uspower.models

import io.github.vinceglb.filekit.core.PlatformFile

sealed interface FileAbstraction {
    class Local(val platformFile: PlatformFile, val byteArray: ByteArray): FileAbstraction
    class CroppedImage(val byteArray: ByteArray): FileAbstraction
    class Remote(val url: String): FileAbstraction
    data object None: FileAbstraction
}