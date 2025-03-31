package com.example.uspower.features.createchat.chatname

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.uspower.ui.BackButtonContainer
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.logo_us_power_2


@Composable
fun ChatNameContent(component: ChatNameComponent, modifier: Modifier = Modifier) {
    val chatName by component.chatName.collectAsState()
    return BackButtonContainer(
        onBackPressed = {component.goBackClicked()},
        modifier = modifier,
        content = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                Image(
                    painter = painterResource(Res.drawable.logo_us_power_2),
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp)
                )

                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    value = chatName,
                    singleLine = true,
                    onValueChange = {component.onNameChanged(it)},
                    label = { Text("Chat name:") }
                )


                Box(modifier = Modifier.fillMaxSize().padding(bottom = 16.dp)) {
                    Button(
                        enabled = chatName.isNotEmpty(),
                        onClick = {
                            component.onNextClicked()
                        },
                        modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                        content = {
                            Text("Next")
                        }
                    )
                }
            }

        }
    )
}