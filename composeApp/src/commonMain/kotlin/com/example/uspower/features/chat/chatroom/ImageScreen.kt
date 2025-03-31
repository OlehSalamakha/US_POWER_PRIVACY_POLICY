package com.example.uspower.features.chat.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uspower.ui.AsyncImageWIthAnim
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.baseline_close_24
import uspower.composeapp.generated.resources.ic_profile_placeholder

@Composable
fun ImageScreen(imageUrl: String, onClose: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().clickable { onClose.invoke() }.background(Color.White).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.clickable { onClose.invoke() }.padding(bottom = 16.dp).align(Alignment.TopCenter),
            painter = painterResource(Res.drawable.baseline_close_24), contentDescription = "Close"
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ) {
            AsyncImageWIthAnim(
                modifier = Modifier.fillMaxWidth(),
                model = imageUrl,
                contentDescription = null,
                error = painterResource(Res.drawable.ic_profile_placeholder),
            )
        }
    }
}
