package com.example.uspower.features.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.logo_us_power_2

@Composable
fun SignUpContent(component: SignUpComponent) {
    val firstName by component.firstName.collectAsState(Dispatchers.Main.immediate)

    val lastName by component.lastName.collectAsState(Dispatchers.Main.immediate)

    val mail by component.mailAddress.collectAsState(Dispatchers.Main.immediate)

    val confirmationMail by component.confirmMailAddress.collectAsState(Dispatchers.Main.immediate)

    val password by component.password.collectAsState(Dispatchers.Main.immediate)


    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Center) {
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

        OutlinedTextField(

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),

            value = firstName,
            onValueChange = { component.onFirstNameChanged(it) },
            label = { Text("First Name") },
            placeholder = { Text("John") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),

            value = lastName,
            onValueChange = { component.onLastNameChange(it) },
            label = { Text("Last Name") },
            placeholder = { Text("") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),

            value = mail,
            onValueChange = { component.onMailAddressChanged(it) },
            label = { Text("Email") },
            placeholder = { Text("email@example.com") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        OutlinedTextField(

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),

            value = confirmationMail,
            onValueChange = { component.onConfirmationMailAddressChanged(it) },
            label = { Text("Email") },
            placeholder = { Text("email@example.com") },
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
            onValueChange = { component.onPasswordChanged(it) },
            label = { Text("password") },
            placeholder = { Text("password") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )


        Button(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            onClick = {
                component.onContinueClicked()
            },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Continue", color = Color.White, modifier = Modifier.padding(8.dp))
        }


    }
}