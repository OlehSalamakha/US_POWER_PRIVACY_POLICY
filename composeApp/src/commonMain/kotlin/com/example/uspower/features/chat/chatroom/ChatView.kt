package com.example.uspower.features.chat.chatroom

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import com.example.uspower.hideKeyboard
import com.example.uspower.models.Chat
import com.example.uspower.models.Message
import com.example.uspower.models.MessageType
import com.example.uspower.models.toType
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.chat_background_main
import uspower.composeapp.generated.resources.ic_arrow_left
import uspower.composeapp.generated.resources.logo_us_power_1

@Composable
fun ChatView(
    modifier: Modifier,
    component: ChatRoomComponent,
    showAttachFile: Boolean,
    isLoading: Boolean,
    chat: Chat,
    listState: LazyListState,
    messagesList: SnapshotStateList<Message>,
    fullScreenImage: (String) -> Unit,
    launcher: () -> Unit,
    fileLauncher: () -> Unit,
    toggleAttachFile: (Boolean) -> Unit
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ) {
        var showSheet by remember { mutableStateOf(false) }
        if (showSheet) {
            BottomSheetChatMenu(component) {
                showSheet = false
            }
        }
        if (showAttachFile) {
            BottomSheetSendFileMenu(
                component,
                onDismissRequest = { toggleAttachFile.invoke(false) },
                launcher = {
                    toggleAttachFile.invoke(false)
                    launcher.invoke()
                },
                fileLauncher = {
                    toggleAttachFile.invoke(false)
                    fileLauncher.invoke()
                }
            )
        }

        val (messages, chatBox, toolbar, image, progress) = createRefs()
        AppBar(chat = chat,
            component = component,
            modifier = modifier
                .fillMaxWidth()
                .constrainAs(toolbar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .constrainAs(progress) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
        BackGround(modifier = modifier
            .fillMaxWidth()
            .constrainAs(image) {
                top.linkTo(toolbar.bottom)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = fillToConstraints
            })

        ChatList(
            listState = listState,
            messagesList = messagesList,
            component = component,
            constrainAs = Modifier
                .fillMaxWidth()
                .constrainAs(messages) {
                    top.linkTo(toolbar.bottom)
                    bottom.linkTo(chatBox.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = fillToConstraints
                },
            fullScreenImage = { fullScreenImage.invoke(it) },
            showSheet = { showSheet = true }
        )
        ChatBox(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .constrainAs(chatBox) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            { message ->
                val trimmed = message.trim()
                if (trimmed.isNotEmpty()) {
                    component.sendMessage(message)
                    hideKeyboard()
                }
            },
            onAttachFile = {
                toggleAttachFile.invoke(true)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(chat: Chat, component: ChatRoomComponent, modifier: Modifier) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.logo_us_power_1),
                    contentDescription = "BackGround"
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(chat.name, fontSize = 16.sp)
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            component.onEditChatClicked()

                        }
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "",
                        )
                    }
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                component.onGoBackClicked()
            }) {
                Image(painter = painterResource(Res.drawable.ic_arrow_left), contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = Color.White
        ),
        modifier = modifier
    )
}

@Composable
fun ChatList(listState: LazyListState, messagesList: SnapshotStateList<Message>, component: ChatRoomComponent, constrainAs: Modifier, fullScreenImage: (String) -> Unit, showSheet: () -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.Bottom,
        state = listState,
        reverseLayout = true,
        modifier = constrainAs,
    ) {
        itemsIndexed(messagesList) { index, item ->
            ChatItem(
                item, component, Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            when (item.type.toType()) {
                                MessageType.TEXT -> {
                                    hideKeyboard()
                                }

                                MessageType.IMAGE -> {
                                    fullScreenImage.invoke(item.content)
                                    hideKeyboard()
                                }

                                MessageType.FILE -> {
                                    component.downloadAndOpen(item)
                                    hideKeyboard()
                                }
                            }
                        },
                        onLongPress = {
                            component.selectedMessage.value = messagesList[index]
                            showSheet.invoke()
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun BackGround(modifier: Modifier) {
    Image(
        painter = painterResource(Res.drawable.chat_background_main),
        contentDescription = "BackGround",
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}