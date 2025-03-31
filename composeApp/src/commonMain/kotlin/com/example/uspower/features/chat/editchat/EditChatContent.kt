package com.example.uspower.features.chat.editchat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.attafitamim.krop.core.crop.CropError
import com.attafitamim.krop.core.crop.CropResult
import com.attafitamim.krop.core.crop.cropSrc
import com.attafitamim.krop.core.crop.rememberImageCropper
import com.attafitamim.krop.core.images.ImageBitmapSrc
import com.attafitamim.krop.ui.ImageCropperDialog
import com.example.uspower.core.LoadState
import com.example.uspower.features.editprofile.CropAppBar
import com.example.uspower.models.FileAbstraction
import com.example.uspower.models.User
import com.example.uspower.toByteArray
import com.example.uspower.ui.BackButtonContainer
import com.example.uspower.ui.ProgressContainer
import com.example.uspower.ui.USPowerProfileImage
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_profile_placeholder
import uspower.composeapp.generated.resources.logo_us_power_2


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun EditChatContent(
    component: EditChatComponent,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val imageCropper = rememberImageCropper()

    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    var error by remember { mutableStateOf<CropError?>(null) }



    val chatName by component.chatName.collectAsState(Dispatchers.Main.immediate)
    val members by component.chatMembers.collectAsState(Dispatchers.Main.immediate)
    val isOwnerOrAdmin by component.adminOrOwner.collectAsState(Dispatchers.Main.immediate)
    val showDeleteChatConfirmationDialog by component.showDeleteChatConfirmationDialog.collectAsState(Dispatchers.Main.immediate)
    val showProgress by component.showProgress.collectAsState(Dispatchers.Main.immediate)


    val imageState by component.chatAvatarState.collectAsState(Dispatchers.Main.immediate)


    val launcher = rememberFilePickerLauncher(mode = PickerMode.Single, type = PickerType.Image) { file ->

        file?.let {
            scope.launch {
                when (val result = imageCropper.cropSrc(ImageBitmapSrc(file.readBytes().decodeToImageBitmap()))) {
                    CropResult.Cancelled -> {}
                    is CropError -> error = result
                    is CropResult.Success -> {
                        println("100500, crop result is success")
                        val byteArray = result.bitmap.toByteArray()
                        println("100500, on image cropped")
                        component.onImageCropped(byteArray)
                        selectedImage = result.bitmap
                    }
                }
            }
        }

    }

    imageCropper.cropState?.let {
        ImageCropperDialog(

            state = it,
            topBar = { CropAppBar(it) }
        )
    }


    if (showDeleteChatConfirmationDialog) {
        BasicAlertDialog(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
            ,
            onDismissRequest = {
                component.onDeleteDialogDismissed()
            },
            content = {
                Column {
                    Text("Delete Chat", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Do you want to Delete Chat?")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(
                            onClick =  {
                                component.onDeleteDialogDismissed()
                            },
                            content = {
                                Text("No")
                            }
                        )

                        TextButton(
                            onClick =  {
                                component.onDeleteChatConfirmed()
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

    ProgressContainer(content = {
        BackButtonContainer(
            modifier = modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp).verticalScroll(rememberScrollState()),
            onBackPressed = {
                component.backClicked()
            },
            content = {
                Box(modifier = Modifier.fillMaxWidth()) {
                    val model = when (imageState) {
                        is FileAbstraction.Local -> (imageState as FileAbstraction.Local).byteArray
                        FileAbstraction.None -> null
                        is FileAbstraction.Remote -> (imageState as FileAbstraction.Remote).url
                        is FileAbstraction.CroppedImage ->  (imageState as FileAbstraction.CroppedImage).byteArray
                    }
                    AsyncImage(
                        model = model,
                        contentDescription = null,
                        contentScale = if (model == null) ContentScale.Fit else ContentScale.Crop,
                        placeholder = painterResource(Res.drawable.logo_us_power_2),
                        error =  painterResource(Res.drawable.logo_us_power_2),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .height(350.dp)
                            .width(350.dp)
//                            .padding(bottom = 8.dp)
                            .align(Alignment.Center)
                            .clickable {
                                launcher.launch()
                            }

                    )
                }


                Spacer(modifier = Modifier.padding(bottom = 8.dp))
                val currentChatName = when(chatName) {
                    is LoadState.Error -> {
                        ""
                        //TODO
                    }
                    LoadState.Loading -> {
                        ""
                        //TODO
                    }
                    is LoadState.Success -> {
                        (chatName as LoadState.Success<String>).data
                    }
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    value = currentChatName,
                    onValueChange = {
                        component.onChatNameChanged(it)
                    },
                    singleLine = true,
                    label = { Text("Chat name: ") }
                )

                Spacer(modifier.height(8.dp))
                Text("Chat members:")

                when(members) {
                    is LoadState.Error -> {
                        //TODO
                    }
                    LoadState.Loading -> {
                        //TODO
                    }
                    is LoadState.Success -> {
                        (members as LoadState.Success<List<User>>).data.forEach {
                            ChatMemberItem(component, isOwnerOrAdmin, it, )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }



                if (isOwnerOrAdmin) {
                    Button(
                        colors = buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Text("Delete Chat")
                        },
                        onClick = {
                            component.onDeleteChatClicked()
                        }
                    )
                }


                OutlinedButton(

                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text("Add Members")
                    },
                    onClick = {
                        component.addMembers()
                    }
                )
            },

            )
    },
        showProgress = showProgress,
        modifier = Modifier.fillMaxWidth()
        )


}


@Composable
fun ChatMemberItem(component: EditChatComponent, showDeleteButton: Boolean, user: User, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )) {

        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
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
            Text(
                "${user.firstName} ${user.lastName}",
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp).weight(1f)
            )

            if (showDeleteButton) {

                Box() {
                    OutlinedButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        colors = buttonColors(
                            contentColor = MaterialTheme.colorScheme.error,
                            containerColor = Color.Transparent
                        ),

                        onClick =  {
                            component.deleteUserFromChat(user)
                        },
                        content = {
                            Text("Remove", )
                        }
                    )
                }

            }
        }
    }
}