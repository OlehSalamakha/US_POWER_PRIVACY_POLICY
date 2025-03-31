package com.example.uspower.features.chat.chatroom

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.example.uspower.camera.CameraManager
import com.example.uspower.camera.rememberCameraManager
import com.example.uspower.models.Message
import com.example.uspower.ui.LoaderFullScreen
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged

const val MESSAGE_COUNT_FOR_LOAD = 3

@Composable
fun ChatRoomScreen(component: ChatRoomComponent, modifier: Modifier = Modifier) {
    val chat by component.chatFlow.collectAsState(Dispatchers.Main.immediate)
    val showCamera by component.cameraShowState.collectAsState(Dispatchers.Main.immediate)

    val messagesList = remember { component.messages }
    val isDownloadFile by remember { component.isDownloadFile }
    val isLoading by remember { component.isLoad }

    var fullImageToFullScreen by remember { mutableStateOf<String?>(null) }
    var showAttachFileBottomMenu by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val imageLauncher = rememberFilePickerLauncher(mode = PickerMode.Single, type = PickerType.Image) { file ->
        file?.let {
            showAttachFileBottomMenu = false
            component.onImageLoaded(it)
        }
    }
    val cameraManager = rememberCameraManager {
        it?.let {
            component.onPhotoTaken(it)
        }
    }
    val fileLauncher = rememberFilePickerLauncher(mode = PickerMode.Single, type = PickerType.File()) { file ->
        file?.let {
            showAttachFileBottomMenu = false
            component.onFileLoaded(it)
        }
    }
    if (showCamera) { showCamera(cameraManager, component) }

    launchEffectScrollWhenSend(messagesList, listState)

    launchEffectPagination(listState, component, messagesList)

    ChatView(
        modifier = modifier,
        component = component,
        showAttachFile = showAttachFileBottomMenu,
        isLoading = isLoading,
        chat = chat,
        listState = listState,
        messagesList = messagesList,
        fullScreenImage = { fullImageToFullScreen = it },
        launcher = { imageLauncher.launch() },
        fileLauncher = { fileLauncher.launch() },
        toggleAttachFile = { showAttachFileBottomMenu = it },
    )

    fullImageToFullScreen?.let { url -> ImageScreen(url) { fullImageToFullScreen = null } }
    if (isDownloadFile) {
        LoaderFullScreen()
    }
}

@Composable
private fun launchEffectPagination(
    listState: LazyListState,
    component: ChatRoomComponent,
    messagesList: SnapshotStateList<Message>
) {
    LaunchedEffect(listState) {
        component.loadMessages()
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleIndex ->
                if (messagesList.size - MESSAGE_COUNT_FOR_LOAD > 0 && lastVisibleIndex != null && lastVisibleIndex >= messagesList.size - MESSAGE_COUNT_FOR_LOAD) {
                    component.loadMessages(messagesList[lastVisibleIndex])
                }
            }
    }
}

@Composable
private fun launchEffectScrollWhenSend(messagesList: SnapshotStateList<Message>, listState: LazyListState) {
    LaunchedEffect(messagesList.size) {
        if (listState.layoutInfo.visibleItemsInfo.isNotEmpty() && messagesList.isNotEmpty() && (listState.layoutInfo.visibleItemsInfo.first().index == 0)) {
            listState.animateScrollToItem(0)
        }
    }
}

private fun showCamera(cameraManager: CameraManager, component: ChatRoomComponent) {
    cameraManager.launch()
    component.resetCamera()
}
