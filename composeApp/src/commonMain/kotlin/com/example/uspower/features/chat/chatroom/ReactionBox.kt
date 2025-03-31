package com.example.uspower.features.chat.chatroom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uspower.models.ReactionEmoji

@Composable
fun ReactionBox(reaction: ReactionEmoji, count: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(1.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomEnd = 20.dp,
                    bottomStart = 20.dp
                )
            )
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(text = reaction.code + "$count", fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp), color = Color.White,
            style = LocalTextStyle.current.copy(
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both,
                )
            ),)
    }
}