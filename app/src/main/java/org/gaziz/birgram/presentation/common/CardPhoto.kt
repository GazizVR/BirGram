package org.gaziz.birgram.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R
import org.gaziz.birgram.domain.model.chat.ChatPhoto

@Composable
fun ChatCardPhoto(
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
fun CardPhoto(
    photo: ChatPhoto?,
    chatTitle: String,
    iconSize: Dp,
    downloadPhoto: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        if(photo != null) {
            if(
                photo.small.canDownload &&
                !photo.small.isDownloading &&
                !photo.small.isCompleted
            ) {
                downloadPhoto(photo.small.id)
            }
        }
    }
    Box {
        if (photo != null) {
            if (photo.small.isCompleted) {
                ChatCardPhoto(photo.small.path,iconSize)
            } else {
                if (photo.miniThumbnail != null) {
                    ChatCardPhoto(photo.miniThumbnail,iconSize)
                } else {
                    PhotoPlaceholder(iconSize, chatTitle)
                }
            }
        } else {
            PhotoPlaceholder(iconSize, chatTitle)
        }
    }
}