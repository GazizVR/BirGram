package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun MessageThumbnail(
    content: Any?,
    contentText: String
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AsyncImage(
            model = content,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = contentText,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.W400,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun MessageText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
){
    Text(
        text = text,
        color = color,
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Start,
        fontSize = 10.sp,
        modifier = modifier
    )
}
