package com.example.uspower.features.chat.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.uspower.models.LOADING_IMAGE
import com.example.uspower.models.Message
import com.example.uspower.models.MessageType
import com.example.uspower.models.toReactionEmoji
import com.example.uspower.models.toType
import com.example.uspower.toHourOfDay
import com.example.uspower.ui.AsyncImageWIthAnim
import com.example.uspower.ui.ShimmerEffect
import dev.gitlive.firebase.firestore.toDuration
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_profile_placeholder


@Composable
fun ChatItem(message: Message, component: ChatRoomComponent, modifier: Modifier) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateAndUserName(message)
        }
        Row(
            modifier = modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = if (message.isFromMe) Arrangement.End else Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                message.type.toType() == MessageType.IMAGE -> ImageItem(message)
                message.type.toType() == MessageType.FILE -> FileItem(message)
                message.type == LOADING_IMAGE -> {
                    Column {
                        ShimmerEffect(200.dp, 200.dp)
                        Spacer(modifier.padding(4.dp))
                    }
                }
                else -> TextMessage(message, component)
            }
        }
    }
}

@Composable
fun EmojiSection(message: Message, onSizeChanged: (IntSize) -> Unit = {}) {
    Row(modifier = Modifier.padding(4.dp).offset(y = (-14).dp).onSizeChanged(onSizeChanged)) {
        val reactionEmojiListMap = message.reactions?.groupBy { it.toReactionEmoji() }
        reactionEmojiListMap?.forEach {
            ReactionBox(it.key, it.value.size)
        }
    }
}

@Composable
fun DateSectionForFileAndImage(message: Message, modifier: Modifier) {
    Row(modifier = modifier.padding(end = 4.dp, bottom = 4.dp), horizontalArrangement = Arrangement.End) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primary).padding(vertical = 4.dp, horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message.timestamp.toDuration().toHourOfDay(), fontSize = 8.sp, color = Color.White,
                style = LocalTextStyle.current.copy(
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.Both,
                    )
                )
            )
        }
    }
}

@Composable
fun DateSectionForText(message: Message) {
    Box(contentAlignment = Alignment.BottomStart) {
        Text(
            text = message.timestamp.toDuration().toHourOfDay(), fontSize = 8.sp, color = if (!message.isFromMe) Color.Black else Color.White,
            style = LocalTextStyle.current.copy(
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both,
                )
            )
        )
    }
}

@Composable
fun ImageItem(message: Message) {
    Column {
        Box {
            AsyncImageWIthAnim(
                model = message.content,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.ic_profile_placeholder),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .size(200.dp)
            )
            DateSectionForFileAndImage(message = message, modifier = Modifier.align(Alignment.BottomEnd))
        }
        EmojiSection(message)
    }
}

@Composable
fun FileItem(message: Message) {
    Column {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.clip(
                RoundedCornerShape(
                    48f
                )
            ).background(Color.White).size(200.dp)
        ) {
            Text(text = message.fileName, modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
            DateSectionForFileAndImage(message = message, modifier = Modifier.align(Alignment.BottomEnd))
        }
        EmojiSection(message)
    }
}

@Composable
fun TextMessage(message: Message, component: ChatRoomComponent) {
    if (!message.isFromMe) {
        AsyncImage(
            model = message.senderAvatar,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(Res.drawable.ic_profile_placeholder),
            error = painterResource(Res.drawable.ic_profile_placeholder),
            modifier = Modifier
                .clip(CircleShape)
                .size(32.dp)
                .clickable {
                    component.onUserClicked(message.sender)
                }
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
    Box(
        modifier = Modifier.wrapContentWidth()
    ) {
        val rowWidth = remember { mutableStateOf(0) }
        Column(horizontalAlignment = Alignment.End) {
            Column(
                modifier = Modifier.wrapContentSize().widthIn(min = with(LocalDensity.current) { rowWidth.value.toDp() })
                    .clip(
                        RoundedCornerShape(
                            topStart = 48f,
                            topEnd = 48f,
                            bottomStart = if (message.isFromMe) 48f else 0f,
                            bottomEnd = if (message.isFromMe) 0f else 48f
                        )
                    )
                    .background(if (!message.isFromMe) Color.White else MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            ) {
                Text(
                    color = if (!message.isFromMe) Color.Black else Color.White,
                    text = message.content
                )
                DateSectionForText(message)
            }
            EmojiSection(message) { rowWidth.value = it.width }
        }
    }
}

@Composable
fun DateAndUserName(message: Message) {
    Column {
        message.date?.let {
            Text(text = it, modifier = Modifier.fillMaxWidth(), fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
        }
        if (!message.senderName.isNullOrEmpty()){
            message.senderName?.let {
                Text(text = it, modifier = Modifier.fillMaxWidth().padding(start = 48.dp).offset(y = 10.dp), fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Start)
            }
        }
    }
}
