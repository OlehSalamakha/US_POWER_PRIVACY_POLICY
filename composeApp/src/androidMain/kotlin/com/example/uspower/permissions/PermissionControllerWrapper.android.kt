package com.example.uspower.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

@Composable
actual fun createPermissionControllerWrapper(): PermissionControllerWrapper {
    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }

    return CommonPermissionControllerWrapper(controller)
}


class CommonPermissionControllerWrapper(
    private val permissionController: PermissionsController
): PermissionControllerWrapper {
    override suspend fun providePermission() {
        try {
            permissionController.providePermission(Permission.CAMERA)
        } catch (deniedAlways: dev.icerock.moko.permissions.DeniedAlwaysException) {
            throw DeniedAlwaysException()
        } catch (denied: dev.icerock.moko.permissions.DeniedException) {
            throw DeniedException()
        }

    }

}
