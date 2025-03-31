package com.example.uspower.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ShimmerEffect(width: Dp, height: Dp) {
    val transition = rememberInfiniteTransition()
    val shimmerX by transition.animateFloat(
        initialValue = -200f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(Color.LightGray, Color.Gray, Color.LightGray),
        start = Offset(shimmerX, 0f),
        end = Offset(shimmerX + 200f, 0f)
    )

    Box(
        modifier = Modifier
            .size(width, height)
            .clip(RoundedCornerShape(16.dp))
            .background(brush)
    )
}

@Composable
fun AsyncImageWIthAnim(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = null,
    contentScale: ContentScale = ContentScale.Fit
) {
    val transition = rememberInfiniteTransition()
    val shimmerX by transition.animateFloat(
        initialValue = -200f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = listOf(Color.LightGray, Color.Gray, Color.LightGray),
        start = Offset(shimmerX, 0f),
        end = Offset(shimmerX + 200f, 0f)
    )
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = placeholder,
        error = error,
        modifier = modifier.background(brush)
    )
}

@Composable
fun LoaderFullScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(enabled = false) {},
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


