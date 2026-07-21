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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto
import org.gaziz.birgram.core.ui.icons.skull

@Composable
fun ChatImage(
    modifier: Modifier,
    photoModel: Any?,
    isDeleted: Boolean,
    placeHolderColor: Color,
    placeHolderText: String
) {
    if(
        photoModel is String ||
        photoModel is ByteArray
    ) {
        AsyncImage(
            model = photoModel,
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
            if(isDeleted) {
                Icon(
                    imageVector = skull,
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
    fontSize: TextUnit,
    isDeleted: Boolean
) {
    val userTypeCnt = stringArrayResource(R.array.user_types)
    Text(
        text = if(isDeleted) userTypeCnt[1] else title,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = fontSize,
    )
}

@Composable
fun ChatPhoto(
    modifier: Modifier,
    photo: ChatPhoto?,
    onPhotoNull: (Int) -> Unit,
    isDeleted: Boolean,
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
        photoModel = chatPhoto,
        isDeleted = isDeleted,
        placeHolderColor = placeHolderColor,
        placeHolderText = placeHolderText
    )
}

@Composable
fun ChatCard(
    modifier: Modifier,
    isDeleted: Boolean,
    photo: ChatPhoto?,
    onPhotoNull: (Int) -> Unit,
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
            ChatPhoto(
                modifier = Modifier.size(photoSize),
                photo = photo,
                onPhotoNull = onPhotoNull,
                isDeleted = isDeleted,
                placeHolderColor = MaterialTheme.colorScheme.primary,
                placeHolderText = if(title.isNotBlank()) title[0].toString() else "",
            )
            Spacer(Modifier.width(8.dp))
            ChatTitle(
                title = title,
                fontSize = titleFontSize,
                isDeleted = isDeleted
            )
        }
    }
}