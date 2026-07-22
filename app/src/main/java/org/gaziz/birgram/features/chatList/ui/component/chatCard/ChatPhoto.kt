package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import coil3.compose.AsyncImage
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto

@Composable
fun ChatAvatar(
    modifier: Modifier,
    imageModel: Any?,
    placeHolderColor: Color,
    placeHolderText: String,
    overlay: @Composable () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if(
            imageModel is String ||
            imageModel is ByteArray
        ) {
            AsyncImage(
                model = imageModel,
                contentDescription = null,
                modifier = modifier.clip(CircleShape),
            )
        } else {
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .background(placeHolderColor),
                contentAlignment = Alignment.Center
            ) {
                if(imageModel is ImageVector){
                    Icon(
                        imageVector = imageModel,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    Text(
                        text = placeHolderText,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        overlay()
    }
}

@Composable
fun ChatPhoto(
    modifier: Modifier,
    photo: ChatPhoto?,
    onPhotoNull: (Int) -> Unit,
    placeHolderColor: Color,
    placeHolderText: String,
    overlay: @Composable () -> Unit
){
    LaunchedEffect(Unit) {
        if(
            photo?.small?.path?.isBlank() == true &&
            photo.small.canDownload
        ) {
            onPhotoNull(photo.small.id)
        }
    }
    val chatPhoto = if(photo?.small?.path?.isNotBlank() == true) {
        photo.small.path
    } else {
        photo?.miniThumbnail
    }
    ChatAvatar(
        modifier = modifier,
        imageModel = chatPhoto,
        placeHolderColor = placeHolderColor,
        placeHolderText = placeHolderText,
        overlay = overlay
    )
}
