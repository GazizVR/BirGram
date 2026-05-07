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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.domain.model.chatList.ChatData

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
            //ChatPhoto
            Box(Modifier.weight(secondWeight)) {
                if (chatData.photo != null) {
                    if (chatData.photo.small.isCompleted) {
                        ChatPhoto(chatData.photo.small.path,iconSize)
                    } else {
                        if (chatData.photo.miniThumbnail != null) {
                            ChatPhoto(chatData.photo.miniThumbnail,iconSize)
                        } else {
                            PhotoPlaceholder(iconSize, chatData.title)
                        }
                    }
                } else {
                    PhotoPlaceholder(iconSize, chatData.title)
                }
            }
            //Main content
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(primaryWeight)
            ) {
                //Top Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Box(Modifier.weight(0.8f)) { ChatCardText(text = chatData.title, alpha = 1f) }
                    if(chatData.lastMessage != null) {
                        ChatCardText(
                            Modifier.weight(0.25f),
                            chatData.lastMessage.date,
                            0.35f,
                            TextAlign.End
                        )
                    }
                }
                //Bottom Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val isUnreadBox =
                        chatData.unreadCount > 0 ||
                        chatData.reactionCount > 0 ||
                        chatData.mentionCount > 0

                    if(chatData.lastMessage != null) {
                        LastMsgContent(
                            Modifier.weight(if(isUnreadBox) 0.8f else 1f),
                            chatData.lastMessage
                        )
                    }

                    if(isUnreadBox) {
                        Box(
                            Modifier.weight(0.15f),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainer)
                                    .size(26.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    when {
                                        chatData.reactionCount > 0 -> "❤\uFE0F"
                                        chatData.unreadCount > 0 -> chatData.unreadCount.toString()
                                        else -> "•"
                                    },
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 8.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
