package com.example.uspower.features.chat.editchat.addmembers.addActionComponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers


@Composable
fun AddActionContent(component: AddActionComponent, modifier: Modifier) {
    val enabled by component.enableAddButton.collectAsState(Dispatchers.Main.immediate)
    Box(modifier = modifier.fillMaxWidth().padding(bottom = 16.dp)) {
        Button(
            enabled = enabled,
            onClick = {
                component.addMembers()
            },
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
            content = {
                Text("Add new Members")
            }
        )
    }
}