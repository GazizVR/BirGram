package org.gaziz.birgram.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.model.ChatPhoto

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
                style = MaterialTheme.typography.labelSmall,
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
fun ChatAvatar(
    photo: ChatPhoto?,
    chatTitle: String,
    iconSize: Dp,
    downloadPhoto: (Int) -> Unit,
    isOnline: Boolean,
    parentColor: Color = Color.Transparent
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
        AnimatedVisibility(
            visible = isOnline,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary,CircleShape)
                    .size(iconSize/4)
                    .border(2.dp, parentColor,CircleShape)
            )
        }
    }
}