package com.example.uspower.features.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.logo_us_power_2


@Composable
fun LoginContent(loginComponent: LoginComponent) {
    val mail by loginComponent.mail.collectAsState(Dispatchers.Main.immediate)

    val scope = rememberCoroutineScope()
    var isLoading by remember { loginComponent.isLoad }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(Res.drawable.logo_us_power_2),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .align(Alignment.Center)
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 32.dp)){
                CircularProgressIndicator()
            }
        } else {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                value = mail,
                onValueChange = { loginComponent.onMailChanged(it) },
                label = { Text("Email") },
                placeholder = { Text("something@mail.com") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )


            Button(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                onClick = {
                    loginComponent.onContinueClicked()
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Continue", color = Color.White, modifier = Modifier.padding(8.dp))
            }
        }


    }

}