package com.example.uspower.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onSurface = SurfaceColor,
    primaryContainer = PrimaryColor,
    surface = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    onSurface = SurfaceColor,
    primaryContainer = PrimaryColor,
    surface = Color.White,
    error = ErrorColor
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (false) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}