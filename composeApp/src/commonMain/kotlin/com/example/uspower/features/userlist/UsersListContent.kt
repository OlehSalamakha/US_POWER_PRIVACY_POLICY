package com.example.uspower.features.userlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.uspower.ui.SearchGray
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_profile_placeholder


@Composable
fun UsersScreen(usersListComponent: UsersListComponent, modifier: Modifier = Modifier) {

    val users by usersListComponent.users.collectAsState(Dispatchers.Main.immediate)
    val userSearch by usersListComponent.userSearch.collectAsState(Dispatchers.Main.immediate)
    Column(
        modifier = modifier.fillMaxWidth().padding(start = 16.dp, top = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Top) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            value = userSearch,
            onValueChange = {
                usersListComponent.onSearchChanged(it)
            },
            placeholder = { Text("Search by user name") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = SearchGray,
                focusedBorderColor = SearchGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(users.size) { item ->
                UserItem(users.get(item), usersListComponent::onUserClicked, onApproveClicked = usersListComponent::onApproveClicked)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun UserItem(
    uiUser: UIUser,
    onUserClicked: (user: UIUser) -> Unit,
    modifier: Modifier = Modifier,
    onApproveClicked: (user: UIUser) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                onUserClicked(uiUser)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )) {

        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = uiUser.user.photoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.ic_profile_placeholder),
                    error =  painterResource(Res.drawable.ic_profile_placeholder),
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .clip(RoundedCornerShape(8.dp))

                )
                Text(uiUser.user.firstName, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp).weight(1f))


                if (uiUser.showApproveButton) {
                    Box() {
                        Button(
                            modifier = Modifier.align(Alignment.CenterEnd),

                            onClick =  {
                                onApproveClicked(uiUser)
                            },
                            content = {
                                Text("Approve", )
                            }
                        )
                    }
                }

        }
    }
}
