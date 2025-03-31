package com.example.uspower

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.uspower.features.root.RootFlowComponent
import com.example.uspower.features.root.RootContent
import com.example.uspower.ui.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(component: RootFlowComponent) {
    AppTheme {
        println("1000500 App startssss")
        RootContent(component, modifier = Modifier)
    }
}