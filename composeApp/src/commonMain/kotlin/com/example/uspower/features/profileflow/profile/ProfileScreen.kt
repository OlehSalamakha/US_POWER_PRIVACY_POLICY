package com.example.uspower.features.profileflow.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uspower.features.singleuser.InfoText
import com.example.uspower.features.singleuser.LabelText
import com.example.uspower.ui.USPowerProfileImage
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileComponent: ProfileComponent, modifier: Modifier = Modifier) {

    val user by profileComponent.user.collectAsState(Dispatchers.Main.immediate)

    val showDialog by profileComponent.showDialog.collectAsState(Dispatchers.Main.immediate)


    if (showDialog.show) {
        dialog(dismissAction = showDialog.dismissAction, confirmAction = showDialog.confirmAction, title = showDialog.title, label = showDialog.label)
    }
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .fillMaxSize()
    ) {
        USPowerProfileImage(
            model = user.photoUrl,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(Modifier.height(8.dp))

        LabelText("Full name:")
        InfoText("${user.firstName} ${user.lastName}")

        LabelText("About me:")
        InfoText("${user.description}")

        LabelText("Role:")
        InfoText("${user.role}")

        LabelText("Start date:")
        InfoText("${user.startDate}")

        LabelText("Phone number: ")
        InfoText("${user.phone}")

        Spacer(Modifier.height(16.dp))



        if (user.admin) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text("Create Profile")
                },
                onClick = {
                    profileComponent.onCreateProfile()
                }
            )
        }


        Button(
            modifier = Modifier.fillMaxWidth(),
            content = {
                Text("Edit Account")
            },
            onClick = {
                profileComponent.onEditProfile()
            }
        )


        Button(
            colors = buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth(),
            content = {
                Text("Log out")
            },
            onClick = {
                profileComponent.onLogoutClicked()
            }
        )


        Button(
            colors = buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth(),
            content = {
                Text("Delete profile")
            },
            onClick = {
                profileComponent.onDeleteProfileClicked()
            }
        )
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable fun dialog(title: String, label: String, dismissAction: () -> Unit, confirmAction: () -> Unit) {
    BasicAlertDialog(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
        ,
        onDismissRequest = {
            dismissAction()
        },
        content = {
            Column {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Text(label)
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick =  {
                            dismissAction()
                        },
                        content = {
                            Text("No")
                        }
                    )

                    TextButton(
                        onClick =  {
                           confirmAction()
                        },
                        content = {
                            Text("Yes")
                        }
                    )
                }
            }
        }

    )
}