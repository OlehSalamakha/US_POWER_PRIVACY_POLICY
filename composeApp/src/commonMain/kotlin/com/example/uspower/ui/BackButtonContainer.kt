package com.example.uspower.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uspower.shouldShowBackButton


@Composable
fun BackButtonContainer(
    content: @Composable () -> Unit,
    onBackPressed: () -> Unit = {},
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {

    Column(horizontalAlignment = horizontalAlignment) {
        if (shouldShowBackButton) {
            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onBackPressed()
                    }
                ) {

                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primaryContainer,
                    )
                }

            }

        }
        Column(modifier = modifier) {
            content()
        }

    }

}