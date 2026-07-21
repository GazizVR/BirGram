package org.gaziz.birgram.features.chatList.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto

@Composable
fun ChatImage(
    modifier: Modifier,
    imageModel: Any?,
    placeHolderColor: Color,
    placeHolderText: String
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
}

@Composable
fun ChatTitle(
    title: String,
    fontSize: TextUnit
) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = fontSize,
    )
}

@Composable
fun ChatPhoto(
    modifier: Modifier,
    photo: ChatPhoto?,
    onPhotoNull: (Int) -> Unit,
    placeHolderColor: Color,
    placeHolderText: String
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
    ChatImage(
        modifier = modifier,
        imageModel = chatPhoto,
        placeHolderColor = placeHolderColor,
        placeHolderText = placeHolderText
    )
}

@Composable
fun ChatCard(
    modifier: Modifier,
    photoModel: Any?,
    onPhotoNull: (Int) -> Unit = {},
    photoSize: Dp,
    title: String,
    titleFontSize: TextUnit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceContainer),
        shape = RoundedCornerShape(0.dp),
        onClick = onClick
    ) {
        Row(
            modifier = modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            if(photoModel is ChatPhoto?) {
                ChatPhoto(
                    modifier = Modifier.size(photoSize),
                    photo = photoModel,
                    onPhotoNull = onPhotoNull,
                    placeHolderColor = MaterialTheme.colorScheme.primary,
                    placeHolderText = if(title.isNotBlank()) title[0].toString() else "",
                )
            } else {
                ChatImage(
                    modifier = Modifier.size(photoSize),
                    imageModel = photoModel,
                    placeHolderColor = MaterialTheme.colorScheme.primary,
                    placeHolderText = if(title.isNotBlank()) title[0].toString() else "",
                )
            }
            Spacer(Modifier.width(8.dp))
            ChatTitle(
                title = title,
                fontSize = titleFontSize,
            )
        }
    }
}