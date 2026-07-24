package org.gaziz.birgram.core.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun ChatTitle(
    modifier: Modifier,
    title: String,
    color: Color,
    fontSize: TextUnit,
) {
    Text(
        text = title,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        lineHeight = fontSize,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}
