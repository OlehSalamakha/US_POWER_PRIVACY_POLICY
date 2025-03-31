package com.example.uspower.permissions

import androidx.compose.runtime.Composable


interface PermissionControllerWrapper {
    suspend fun providePermission()
}

@Composable
expect fun createPermissionControllerWrapper(): PermissionControllerWrapper


class DeniedAlwaysException: Throwable()

class DeniedException: Throwable()


