package com.example.uspower.features.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.attafitamim.krop.core.crop.CropError
import com.attafitamim.krop.core.crop.CropResult
import com.attafitamim.krop.core.crop.CropState
import com.attafitamim.krop.core.crop.cropSrc
import com.attafitamim.krop.core.crop.rememberImageCropper
import com.attafitamim.krop.core.images.ImageBitmapSrc
import com.attafitamim.krop.ui.ImageCropperDialog
import com.example.uspower.models.FileAbstraction
import com.example.uspower.models.Role
import com.example.uspower.toByteArray
import com.example.uspower.ui.BackButtonContainer
import com.example.uspower.ui.USPowerProfileImage
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun EditProfileContent(component: EditProfileComponent) {
    val scope = rememberCoroutineScope()
    val imageCropper = rememberImageCropper()

    var selectedImage by remember { mutableStateOf<ImageBitmap?>(null) }

    var error by remember { mutableStateOf<CropError?>(null) }

    val firstName by component.firstName.collectAsState(Dispatchers.Main.immediate)

    val lastName by component.lastName.collectAsState(Dispatchers.Main.immediate)

    val aboutMe by component.aboutMe.collectAsState(Dispatchers.Main.immediate)


    val role by component.role.collectAsState(Dispatchers.Main.immediate)

    val dateFormat: DateTimeFormat<LocalDate> = LocalDate.Format {
            monthNumber()
            char('/')
            dayOfMonth()
            char('/')
            year()
        }

    val startDate by component.startDate.collectAsState(Dispatchers.Main.immediate)

    val phoneNumber by component.phoneNumber.collectAsState(Dispatchers.Main.immediate)

    val imageState by component.imageState.collectAsState(Dispatchers.Main.immediate)


    val updateProfileProgress by component.updateProfileProgress.collectAsState(Dispatchers.Main.immediate)


    var roleDropDownExpanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()


    //TODO BAD SOLUTION NEED TO FIND BETTER

    val selectedDate = datePickerState.selectedDateMillis?.let {
        val instant = Instant.fromEpochMilliseconds(it)
        val date= instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        component.onStartDateChanged(date.format(dateFormat))
        date.format(dateFormat)
    } ?: ""


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
           topBar = {CropAppBar(it)}
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackButtonContainer(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), onBackPressed = {component.onBackPressed()}, content = {

            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {

                val model = when (imageState) {
                    is FileAbstraction.Local -> (imageState as FileAbstraction.Local).byteArray
                    FileAbstraction.None -> null
                    is FileAbstraction.Remote -> (imageState as FileAbstraction.Remote).url
                    is FileAbstraction.CroppedImage ->  (imageState as FileAbstraction.CroppedImage).byteArray
                }

                USPowerProfileImage(
                    model = model,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable {
                            launcher.launch()
                        }
                )

                Spacer(Modifier.height(16.dp))
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

                )

                OutlinedTextField(

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    value = lastName,
                    onValueChange = { component.onLastNameChanged(it) },
                    label = { Text("Last Name") },
                    placeholder = { Text("") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                OutlinedTextField(

                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    value = aboutMe,
                    onValueChange = { component.onAboutChanged(it) },
                    label = { Text("Description") },
                    placeholder = { Text("Description") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = startDate,
                        onValueChange = {
                            println("100500 On value change")
                        },
                        label = { Text("Start date") },
                        readOnly = true,
                        placeholder = { Text("Start date") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = {
                                showDatePicker = !showDatePicker
                            }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select date date"
                                )
                            }
                        }
                    )

                    if (showDatePicker) {
                        Popup(
                            onDismissRequest = { showDatePicker = false },
                            alignment = Alignment.TopStart
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().offset(y = -32.dp)
                                    .shadow(elevation = 4.dp)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp)
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    showModeToggle = false
                                )
                            }
                        }

                    }

                }


                ExposedDropdownMenuBox(
                    expanded = roleDropDownExpanded,
                    onExpandedChange = { roleDropDownExpanded = it }
                ) {
                    TextField(
                        value = role.customValue,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                    )

                    ExposedDropdownMenu(
                        expanded = roleDropDownExpanded,
                        onDismissRequest = { roleDropDownExpanded = false }

                    ) {
                        Role.entries.forEach {
                            DropdownMenuItem(
                                text = { Text(it.customValue) },
                                onClick = {
                                    component.onRoleChanged(it)
                                    roleDropDownExpanded = false
                                }
                            )
                        }
                    }

                }
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    value = phoneNumber,
                    onValueChange = { component.onPhoneNumberChanged(it) },
                    label = { Text("Phone") },
                    placeholder = { Text("Phone") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()

                )

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    content = {
                        Text("Change info")
                    },
                    onClick = {
                        component.updateProfile()
                    }
                )

            }
        })

        if (updateProfileProgress) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().background(Color(255, 255, 255, 200))) {
                CircularProgressIndicator()
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropAppBar(state: CropState) {
    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = { state.done(accept = false) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = { state.reset() }) {
                Icon(Icons.Filled.Refresh, null)
            }
            IconButton(onClick = { state.done(accept = true) }, enabled = !state.accepted) {
                Icon(Icons.Default.Done, null)
            }
        }

    )
}
