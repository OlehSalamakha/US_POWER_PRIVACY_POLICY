package com.example.uspower.features.createchat.addandcreate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uspower.features.createchat.addusers.AddUsersToChatContent
import com.example.uspower.features.createchat.create.CreateChatContent
import com.example.uspower.ui.ProgressContainer
import kotlinx.coroutines.Dispatchers


@Composable
fun AddAndCreateChatContent(component: AddAndCreateComponent, modifier: Modifier = Modifier) {
    val showProgress by component.showProgress.collectAsState(Dispatchers.Main.immediate)

    ProgressContainer(
        content = {
            Column(modifier = Modifier.fillMaxSize().padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
                AddUsersToChatContent(component.addUsersComponent, modifier = Modifier.weight(1f))

                CreateChatContent(component.createChatComponent, modifier)
            }
        },
        showProgress = showProgress,
        modifier = Modifier.fillMaxSize()
    )

}