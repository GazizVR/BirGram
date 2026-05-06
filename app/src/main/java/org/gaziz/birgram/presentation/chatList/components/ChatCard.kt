package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R
import org.gaziz.birgram.domain.model.chatList.ChatData
import org.gaziz.birgram.domain.model.chatList.MessageContent

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
                style = MaterialTheme.typography.labelMedium,
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
fun ChatCard(
    chatData: ChatData,
    downloadPhoto: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        if(chatData.photo != null) {
            if(
                chatData.photo.small.canDownload &&
                !chatData.photo.small.isDownloading &&
                !chatData.photo.small.isCompleted
            ) {
                downloadPhoto(chatData.photo.small.id)
            }
        }
    }
    val containerSize = 55.dp
    val cardHeight = 80.dp
    val secondWeight = 0.2f
    val primaryWeight = 0.6f
    Card(
        shape = RoundedCornerShape(0),
        onClick = {},
        modifier = Modifier.height(cardHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(Modifier.weight(secondWeight)) {
                if (chatData.photo != null) {
                    if (chatData.photo.small.isCompleted) {
                        AsyncImage(
                            model = chatData.photo.small.path,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(containerSize)
                                .clip(CircleShape)
                        )
                    } else {
                        if (chatData.photo.miniThumbnail != null) {
                            AsyncImage(
                                model = chatData.photo.miniThumbnail,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(containerSize)
                                    .clip(CircleShape)
                            )
                        } else {
                            PhotoPlaceholder(containerSize, chatData.title)
                        }
                    }
                } else {
                    PhotoPlaceholder(containerSize, chatData.title)
                }
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(primaryWeight)
            ) {
                Text(
                    chatData.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1
                )
                if(chatData.lastMessage != null) {
                    when(chatData.lastMessage.content) {
                        is MessageContent.Other -> Text(
                            chatData.lastMessage.content.type,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        is MessageContent.Text -> Text(
                            chatData.lastMessage.content.text,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        else -> Text(
                            chatData.lastMessage.content.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    Spacer(Modifier)
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(secondWeight)
            ) {
                if(chatData.lastMessage != null) {
                    Text(
                        chatData.lastMessage.date.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                } else {
                    Spacer(Modifier)
                }
                if(
                    chatData.unreadCount > 0 &&
                    chatData.reactionCount > 0 &&
                    chatData.mentionCount > 0
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .size(containerSize),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "●",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                } else {
                    Spacer(Modifier)
                }
            }
        }
    }
}
