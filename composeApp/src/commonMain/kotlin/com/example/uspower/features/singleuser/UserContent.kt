package com.example.uspower.features.singleuser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.example.uspower.ui.BackButtonContainer
import com.example.uspower.ui.USPowerProfileImage
import kotlinx.coroutines.Dispatchers


@Composable
fun UserContent(component: SingleUserComponent, modifier: Modifier = Modifier) {

    val state by component.state.collectAsState(Dispatchers.Main.immediate)

    if (state.showConfirmationDialog) {
        ConfirmationDialog(component)
    }

    BackButtonContainer(
        content = {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                USPowerProfileImage(
                    model = state.user.photoUrl,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                )
                Spacer(Modifier.height(8.dp))

                LabelText("Full name:")
                InfoText("${state.user.firstName} ${state.user.lastName}")

                LabelText("About me:")
                InfoText("${state.user.description}")

                LabelText("Role:")
                InfoText("${state.user.role}")

                LabelText("Start date:")
                InfoText("${state.user.startDate}")

                LabelText("Phone number: ")
                InfoText("${state.user.phone}")

                if (state.showDeleteButton) {
                    Button(
                        colors = buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Text("Delete User")
                        },
                        onClick = {
                            component.deleteUserClicked()
                        }
                    )
                }

                if (state.showApproveButton) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Text("Approve")
                        },
                        onClick = {
                            component.approveUser()
                        }
                    )
                }
            }




        },
        onBackPressed = { component.onGoBackClicked() },
        modifier = modifier,

    )


}

@Composable
fun LabelText(text: String) {
    Text(text, color = MaterialTheme.colorScheme.primary)
}

@Composable
fun InfoText(text: String) {
    Text(text,  color = MaterialTheme.colorScheme.primary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationDialog(component: SingleUserComponent) {
    BasicAlertDialog(
        modifier = Modifier
            .background(Color.White)
            .padding(16.dp)
        ,
        onDismissRequest = {
            component.deleteDialogDismissed()
        },
        content = {
            Column {
                Text("Delete User", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Do you want to Delete User?")
                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick =  {
                            component.deleteDialogDismissed()
                        },
                        content = {
                            Text("No")
                        }
                    )

                    TextButton(
                        onClick =  {
                            component.deleteUserConfirmed()
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