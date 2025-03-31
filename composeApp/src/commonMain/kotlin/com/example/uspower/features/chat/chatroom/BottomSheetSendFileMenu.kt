package com.example.uspower.features.chat.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_camera
import uspower.composeapp.generated.resources.ic_file
import uspower.composeapp.generated.resources.ic_image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSendFileMenu(component: ChatRoomComponent, onDismissRequest: () -> Unit, launcher: () -> Unit, fileLauncher: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { onDismissRequest.invoke() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BottomMenuItem("Add a Photo", painterResource(Res.drawable.ic_image), {
                launcher.invoke()
            })
            BottomMenuItem("Add file", painterResource(Res.drawable.ic_file), {
                fileLauncher.invoke()
            })
            BottomMenuItem("Take photo", painterResource(Res.drawable.ic_camera), {
                onDismissRequest.invoke()
                component.askCameraPermission()
            })
        }
    }
}

@Composable
fun BottomMenuItem(contentDescription: String, painter: Painter, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(contentDescription, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp))
        Image(
            painter = painter,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(end = 16.dp, bottom = 8.dp, top = 8.dp)
        )
    }
}