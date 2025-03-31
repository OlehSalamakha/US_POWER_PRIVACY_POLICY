package com.example.uspower.permissions

import androidx.compose.runtime.Composable

@Composable
actual fun createPermissionControllerWrapper(): PermissionControllerWrapper {
    return object: PermissionControllerWrapper {
        override suspend fun providePermission() {

        }

    }
}