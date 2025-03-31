package com.example.uspower.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun ProgressContainer(
    content: @Composable () -> Unit,
    showProgress: Boolean,
    modifier: Modifier = Modifier) {

    Box(modifier = modifier) {
        content()

        if (showProgress) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().background(Color(255, 255, 255, 200))
            ) {
                CircularProgressIndicator()
            }
        }
    }
}