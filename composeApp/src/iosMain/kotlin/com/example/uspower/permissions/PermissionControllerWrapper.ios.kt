package com.example.uspower.permissions

import androidx.compose.runtime.Composable
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.ios.PermissionsController

@Composable
actual fun createPermissionControllerWrapper(): PermissionControllerWrapper {
    return CommonPermissionControllerWrapper(PermissionsController())
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