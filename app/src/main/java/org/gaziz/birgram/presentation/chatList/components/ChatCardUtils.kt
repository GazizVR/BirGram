package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R

@Composable
fun PhotoPlaceholder(
    containerSize: Dp,
    title: String
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if(title.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
            .size(containerSize),
        contentAlignment = Alignment.Center
    ) {
        if(title.isNotEmpty()) {
            Text(
                title[0].toString(),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
        } else {
            Icon(
                ImageVector.vectorResource(R.drawable.skull),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(containerSize/2)
            )
        }
    }
}

@Composable
fun ChatPhoto(
    photoModel: Any,
    iconSize: Dp
) {
    AsyncImage(
        model = photoModel,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(iconSize)
            .clip(CircleShape)
    )
}

@Composable
fun ChatCardText(
    modifier: Modifier = Modifier,
    text: String,
    alpha: Float = 0.5f,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha)
){
    Text(
        text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontSize = 10.sp,
        maxLines = 1,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}