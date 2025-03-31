package com.example.uspower.features.profileflow.createprofile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.uspower.ui.BackButtonContainer
import com.example.uspower.ui.ProgressContainer
import kotlinx.coroutines.Dispatchers


@Composable
fun CreateProfileContent(component: CreateProfileComponent, modifier: Modifier) {
    val state by component.state.collectAsState(Dispatchers.Main.immediate)

    ProgressContainer(
       content = {
           createProfileScreen(component, state)
       } ,
        showProgress = state.showLoading,
        modifier = modifier
    )

}


@Composable
private fun createProfileScreen(component: CreateProfileComponent, state: CreateProfileState) {
    BackButtonContainer(
        content = {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)){
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),

                    value = state.firstName,
                    onValueChange = { component.changeFirstName(it) },
                    label = { Text("First Name") },
                    placeholder = { Text("John") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),

                    value = state.lastName,
                    onValueChange = { component.changeLastName(it) },
                    label = { Text("Last Name") },
                    placeholder = { Text("John") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),

                    value = state.mail,
                    onValueChange = { component.changeEmail(it) },
                    label = { Text("Email") },
                    placeholder = { Text("email@com") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),

                    value = state.confirmationMail,
                    onValueChange = { component.changeConfirmationMail(it) },
                    label = { Text("Confirm Email") },
                    placeholder = { Text("email@com") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),

                    value = state.password,
                    onValueChange = { component.changePassword(it) },
                    label = { Text("Password") },
                    placeholder = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),

                    value = state.confirmationPassword,
                    onValueChange = { component.changeConfirmationPassword(it) },
                    label = { Text("Confirm Password") },
                    placeholder = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.createButtonEnabled,
                        content = {
                            Text("Invite")
                        },
                        onClick = {
                            component.createProfile()
                        }
                    )
                }
            }
        },
        onBackPressed = {component.backClicked()},
        modifier = Modifier.fillMaxSize()
    )
}