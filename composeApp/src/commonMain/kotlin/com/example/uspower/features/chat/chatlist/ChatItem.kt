package com.example.uspower.features.chat.chatlist


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.uspower.models.Chat
import com.example.uspower.ui.WhiteTransparent
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.logo_us_power_2


@Composable
fun ChatItem(chat: Chat, modifier: Modifier = Modifier, component: ChatListComponent) {
    val bgColor = if (chat.unreadCount > 0) {
        Color(0xFFEFEFFF)
    } else {
        MaterialTheme.colorScheme.surface
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = bgColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )) {

            Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {

                val photoUrl = chat.photoUrl

                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(if (photoUrl.isEmpty()) WhiteTransparent else Color.Transparent) ) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = null,
                        contentScale = if( photoUrl.isEmpty()) ContentScale.Fit else ContentScale.Crop,
                        placeholder = painterResource(Res.drawable.logo_us_power_2),
                        error =  painterResource(Res.drawable.logo_us_power_2),
                        modifier = Modifier
                            .padding(if (photoUrl.isEmpty()) 8.dp else 0.dp)
                            .height(if (photoUrl.isEmpty()) 60.dp else 76.dp)
                            .width(if (photoUrl.isEmpty()) 60.dp else 76.dp)
                    )
                }


//                }
                Column(modifier = Modifier.padding(start = 8.dp).fillMaxHeight(), verticalArrangement = Arrangement.Center) {


                    Text(chat.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                    val text = if (chat.lastSender != null && chat.lastMessage != null) {
                        "${chat.lastSender}: ${chat.lastMessage}"
                    } else {
                        ""
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF808080),
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(0.85f),
                            maxLines = 1,
                        )

                        if (chat.unreadCount != 0) {
                            val unreadCountText = if (chat.unreadCount <= 10) {
                                "${chat.unreadCount}"
                            } else {
                                "10+"
                            }
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xAA808080)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 0.dp
                                )) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    text = unreadCountText,
                                )
                            }
                        }
                    }
                }
            }

    }
}


