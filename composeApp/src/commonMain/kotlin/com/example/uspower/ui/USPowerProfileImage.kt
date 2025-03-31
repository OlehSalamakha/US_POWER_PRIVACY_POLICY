package com.example.uspower.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.uspower.needToCrop
import org.jetbrains.compose.resources.painterResource
import uspower.composeapp.generated.resources.Res
import uspower.composeapp.generated.resources.ic_profile_placeholder


@Composable
fun USPowerProfileImage(modifier: Modifier, model: Any?) {
    AsyncImage(
        model = model,
        contentDescription = null,
        contentScale = if (needToCrop) ContentScale.Crop else ContentScale.Inside,
        placeholder = painterResource(Res.drawable.ic_profile_placeholder),
        error =  painterResource(Res.drawable.ic_profile_placeholder),
        modifier = modifier

    )
}