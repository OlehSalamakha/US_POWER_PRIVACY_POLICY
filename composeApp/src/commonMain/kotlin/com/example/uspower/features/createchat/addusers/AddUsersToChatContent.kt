package com.example.uspower.features.createchat.addusers


import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
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
import com.example.uspower.models.User
import com.example.uspower.ui.SearchGray
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_profile_placeholder


@Composable
fun AddUsersToChatContent(component: AddUsersToChatComponent, modifier: Modifier = Modifier) {

    val users by component.users.collectAsState(Dispatchers.Main.immediate)
    val userSearch by component.userSearch.collectAsState(Dispatchers.Main.immediate)
    val selectedUsers by component.selectedUsers.collectAsState(Dispatchers.Main.immediate)
    Column(
        modifier = modifier,

        verticalArrangement = Arrangement.Top) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            value = userSearch,
            onValueChange = {
                component.onSearchChanged(it)
            },
            placeholder = { Text("Search by user name") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = SearchGray,
                focusedBorderColor = SearchGray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(users.size) { item ->
                val user = users.get(item)
                val selected = selectedUsers.contains(user)
                UserItem(user, component::onUserClicked, selected = selected)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun UserItem(user: User, onUserClicked: (user: User) -> Unit, modifier: Modifier = Modifier, selected: Boolean = false) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
           ,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )) {

        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {

            Row (verticalAlignment = Alignment.CenterVertically){
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(Res.drawable.ic_profile_placeholder),
                    error =  painterResource(Res.drawable.ic_profile_placeholder),
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)
                        .clip(RoundedCornerShape(8.dp))

                )
                Text(user.firstName, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp))
            }

            RadioButton(selected = selected, onClick = {
                onUserClicked(user)

            })
        }
    }
}