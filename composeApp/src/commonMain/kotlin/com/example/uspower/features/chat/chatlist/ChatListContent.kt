package com.example.uspower.features.chat.chatlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.uspower.core.LoadState
import com.example.uspower.models.Chat
import com.example.uspower.ui.SearchGray
import com.example.uspower.ui.SearchTextField
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_add

@Composable
fun ChatListContent(component: ChatListComponent, modifier: Modifier) {

    val seqrchQuery by component.chatSearch.collectAsState(Dispatchers.Main.immediate)

    val chats by component.uiChats.collectAsState(Dispatchers.Main.immediate)
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.Top) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    value = seqrchQuery,
                    onValueChange = {
                        component.onSearchChanged(it)
                    },
                    placeholder = { Text("Search by chat name") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = SearchGray,
                        focusedBorderColor = SearchGray
                    )
                )
            }
            Spacer(Modifier.padding(8.dp))
            Image(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = "Add chat",
                contentScale = ContentScale.Fit,
                modifier = Modifier.clickable {
                    component.onCreateNewChatClicked()
                }
            )
        }

        when(chats) {
            is LoadState.Error -> {
                //TODO
            }
            LoadState.Loading -> {
                //TODO
            }
            is LoadState.Success -> {
                (chats as LoadState.Success<List<Chat>>).data.forEach { chat ->
                    ChatItem(chat, modifier = Modifier.clickable {
                        component.onChatClicked(chat)
                        println("Chat item clickedddd: ${chat.chatId}")
                    }, component = component)
                    Spacer(modifier = Modifier.height(8.dp))
                }

            }
        }


    }
}