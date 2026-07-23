package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit


@Composable
fun ChatTime(
    date: String,
    fontSize: TextUnit
) {
    Text(
        text = date,
        color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
        fontSize = fontSize,
        lineHeight = fontSize,
        maxLines = 1
    )
}