package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.core.ui.icon.playArrow

@Composable
fun PlaybackButton() {
    Box(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.background.copy(0.35f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = playArrow,
            contentDescription = null,
            modifier = Modifier.padding(6.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(0.75f)
        )
    }
}