package com.example.uspower.features.auth.signin


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.logo_us_power_2

@Composable
fun SignInContent(signInComponent: SignInComponent) {
    var isLoading by remember { signInComponent.isLoad }

    println("start content of login screen")
    val mail by signInComponent.mail.collectAsState(Dispatchers.Main.immediate)
    val password by signInComponent.password.collectAsState(Dispatchers.Main.immediate)

    val rememberThisDevice by signInComponent.rememberThisDevice.collectAsState(Dispatchers.Main.immediate)

    val scope = rememberCoroutineScope()


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
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            value = mail,
            onValueChange = { signInComponent.onMailChanged(it) },
            label = { Text("Email") },
            placeholder = { Text("something@mail.com") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )



        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            value = password,
            onValueChange = { signInComponent.onPasswordChanged(it) },
            label = { Text("Password") },
            placeholder = { Text("password") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth().padding(8.dp)

        )



        Button(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            onClick = {
                scope.launch {
                    signInComponent.onLoginClick()
                }
            },
            shape = RoundedCornerShape(8.dp)) {
            Text("Login!", color = Color.White, modifier = Modifier.padding(8.dp))
        }


        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {

            Text("Remember this device", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
            Switch(
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFCAD1DB),
                    uncheckedBorderColor = Color.Transparent
                ),
                checked = rememberThisDevice,
                onCheckedChange = {
                    signInComponent.onRememberThisDeviceChanged(it)
                }

            )
        }


        }
    }

}

