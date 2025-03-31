package com.example.uspower.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.logo_us_power_2


@Composable
fun SplashScreen(component: SplashComponent, modifier: Modifier) {
    component.processSplashScreen()
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()

    ) {
        Image(
            painter = painterResource(Res.drawable.logo_us_power_2),
            contentDescription = "Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .align(Alignment.Center)
        )
    }
}