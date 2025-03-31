package com.example.uspower.features.chat.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_clip
import uspower.composeapp.generated.resources.ic_send

@Composable
fun ChatBox(modifier: Modifier, onSendChatClickListener: (String) -> Unit, onAttachFile: () -> Unit) {
    var chatBoxValue by remember { mutableStateOf(TextFieldValue("")) }
    Row(modifier = modifier.clip(RoundedCornerShape(topStart = 48f, topEnd = 48f)).background(MaterialTheme.colorScheme.primary).padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
        BasicTextField(
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            value = chatBoxValue,
            onValueChange = { chatBoxValue = it },
            modifier = Modifier.weight(1f).clip(
                CircleShape
            ).background(Color.White)
                .padding(horizontal = 6.dp, vertical = 6.dp),
            textStyle = TextStyle(fontSize = 12.sp, color = Color.Black),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.padding(6.dp)
                ) {
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.padding(0.dp)) {
            IconButton(
                modifier = Modifier.clip(
                    CircleShape
                ).background(Color.White),
                content = { Image(painter = painterResource(Res.drawable.ic_clip), contentDescription = "") },
                onClick = {
                    onAttachFile.invoke()
                }
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.padding(0.dp)) {
            IconButton(
                modifier = Modifier.clip(
                    CircleShape
                ).background(Color.White),
                content = { Image(painter = painterResource(Res.drawable.ic_send), contentDescription = "") },
                onClick = {
                    onSendChatClickListener.invoke(chatBoxValue.text)
                    chatBoxValue = TextFieldValue("")
                }
            )
        }
    }
}