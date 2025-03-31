package com.example.uspower.features.chat.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.uspower.copyToClipboard
import com.example.uspower.models.ReactionEmoji
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_copy
import uspower.composeapp.generated.resources.ic_delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetChatMenu(component: ChatRoomComponent, onDismissRequest: () -> Unit) {
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
            Row {
                ReactionEmoji.entries.forEach {
                    var modifier: Modifier = Modifier.clip(
                        RoundedCornerShape(
                            topStart = 24f,
                            topEnd = 24f,
                            bottomStart = 24f,
                            bottomEnd = 24f
                        )
                    )
                    if (component.showPrimaryBackground(it)) {
                        modifier = modifier.background(MaterialTheme.colorScheme.primary)
                    }
                    TextButton(
                        modifier = modifier,
                        content = {
                            Text(it.code)
                        },
                        onClick = {
                            component.addEmoji(it, component.selectedMessage.value)
                            onDismissRequest.invoke()
                        }
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        copyToClipboard(component.selectedMessage.value?.content.orEmpty())
                        onDismissRequest.invoke()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Copy", modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp))
                Image(
                    painter = painterResource(Res.drawable.ic_copy),
                    contentDescription = "Copy",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(end = 16.dp, bottom = 8.dp, top = 8.dp)
                )
            }
            if (component.selectedMessage.value?.isFromMe == true) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            component.deleteMessage(component.selectedMessage.value)
                            onDismissRequest.invoke()
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Delete", modifier = Modifier.padding(start = 16.dp, bottom = 8.dp, top = 8.dp))
                    Image(
                        painter = painterResource(Res.drawable.ic_delete),
                        contentDescription = "Delete",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.padding(end = 16.dp, bottom = 8.dp, top = 8.dp)
                    )
                }
            }
        }
    }
}