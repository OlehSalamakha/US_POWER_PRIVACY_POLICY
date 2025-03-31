package com.example.uspower.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import coil3.toUri



//
//// Platform-specific function to return the correct model
@Composable
expect fun LocalImage(filePath: String, modifier: Modifier)