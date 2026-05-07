package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R
import org.gaziz.birgram.domain.model.chatList.ChatData
import org.gaziz.birgram.domain.model.chatList.MessageContent
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.formatForChatList(locale: Locale = Locale.getDefault()): String {
    val now = LocalDate.now()
    val date = toLocalDate()

    return when {
        date == now -> format(DateTimeFormatter.ofPattern("HH:mm"))

        date.isAfter(now.minusDays(7)) -> format(DateTimeFormatter.ofPattern("EEE", locale))

        date.year == now.year -> format(DateTimeFormatter.ofPattern("d MMM", locale))

        else -> format(DateTimeFormatter.ofPattern("dd.MM.yy"))
    }
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
fun ChatCardText(
    modifier: Modifier = Modifier,
    text: String,
    alpha: Float = 0.5f,
    textAlign: TextAlign = TextAlign.Center,
){
    Text(
        text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha),
        fontSize = 10.sp,
        maxLines = 1,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
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
    val containerHeight = 80.dp
    val iconSize = 60.dp
    val secondWeight = 0.2f
    val primaryWeight = 0.8f
    Card(
        shape = RoundedCornerShape(0),
        onClick = {},
        modifier = Modifier.height(containerHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Box(Modifier.weight(secondWeight)) {
                if (chatData.photo != null) {
                    if (chatData.photo.small.isCompleted) {
                        AsyncImage(
                            model = chatData.photo.small.path,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(iconSize)
                                .clip(CircleShape)
                        )
                    } else {
                        if (chatData.photo.miniThumbnail != null) {
                            AsyncImage(
                                model = chatData.photo.miniThumbnail,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(iconSize)
                                    .clip(CircleShape)
                            )
                        } else {
                            PhotoPlaceholder(iconSize, chatData.title)
                        }
                    }
                } else {
                    PhotoPlaceholder(iconSize, chatData.title)
                }
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(primaryWeight)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Box(Modifier.weight(0.8f)) { ChatCardText(text = chatData.title, alpha = 1f) }
                    if(chatData.lastMessage != null) {
                        ChatCardText(
                            Modifier.weight(0.25f),
                            chatData.lastMessage.date.formatForChatList(),
                            0.35f,
                            TextAlign.End
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val isUnreadBox =
                        chatData.unreadCount > 0 &&
                        chatData.reactionCount > 0 &&
                        chatData.mentionCount > 0

                    if(chatData.lastMessage != null) {
                        Box(Modifier.weight(if(isUnreadBox) primaryWeight else 1f)) {
                            when(chatData.lastMessage.content) {
                                is MessageContent.Other -> ChatCardText(Modifier,chatData.lastMessage.content.type)
                                is MessageContent.Text -> ChatCardText(Modifier,chatData.lastMessage.content.text)
                                else -> ChatCardText(Modifier,chatData.lastMessage.content.toString().substringAfterLast("."))
                            }
                        }
                    }

                    if(isUnreadBox) {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .size(containerHeight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "●",
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
