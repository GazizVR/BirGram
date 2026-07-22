package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun OnlineIndicator(
    size: Dp,
    isOnline: Boolean,
    indicatorColor: Color,
    backgroundColor: Color,
    alignment: Alignment
) {
    AnimatedVisibility(
        visible = isOnline
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = alignment
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(size + 4.dp)
                        .clip(CircleShape)
                        .background(backgroundColor)
                )
                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .background(indicatorColor)
                )
            }
        }
    }
}