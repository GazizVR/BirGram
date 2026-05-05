package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R
import org.gaziz.birgram.domain.model.chatList.ChatData

@Composable
fun ChatCard(
    chatData: ChatData
) {
    val containerSize = 60.dp
    Card(
        shape = RoundedCornerShape(0),
        onClick = {}
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if(chatData.photo.miniThumbnail != null) {
                Box(
                    modifier = Modifier.clip(CircleShape)
                ) {
                    AsyncImage(
                        model = chatData.photo.miniThumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(containerSize)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .size(containerSize),
                    contentAlignment = Alignment.Center
                ) {
                    if(chatData.title.isNotEmpty()) {
                        Text(
                            chatData.title[0].toString(),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                        )
                    } else {
                        Icon(
                            ImageVector.vectorResource(R.drawable.skull),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
            Text(
                chatData.title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
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
            }
        }
    }
}
