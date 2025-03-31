package com.example.uspower.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.ui.graphics.painter.Painter
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import java.io.File

//actual fun getImageModel(filePath: String): Any? {
//    TODO("Not yet implemented")
//}
@Composable
actual fun LocalImage(filePath: String, modifier: Modifier) {

    val directory = "/data/user/0/com.example.uspower/files/"
    val direcotryFile = File(directory)

    println("100500, directory exists: ${direcotryFile.exists()}")
    direcotryFile.listFiles().forEach {
        println("100500, dir file is ${it.name}")
    }
//    val painter = Painter(File(filePath))

    val file = File(filePath)
    if (file.exists()) {
        println("100500, file existssss")
    } else {
        println("100500, flie not exists")
    }

    AsyncImage(
        model = file,
        contentDescription =  null,
        modifier = modifier
    )

//    Image(
//        painter = painter,
//        contentDescription = "Local Image",
//        modifier = modifier
//    )

}