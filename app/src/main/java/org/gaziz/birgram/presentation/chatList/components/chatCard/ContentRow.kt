package org.gaziz.birgram.presentation.chatList.components.chatCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.domain.model.chat.LastMessageData

@Composable
fun UnreadBox(
    modifier: Modifier = Modifier,
    unreadCount: Int,
    reactionCount: Int
) {
    Box(
        modifier = modifier,
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
                    reactionCount > 0 -> "❤\uFE0F"
                    unreadCount > 0 -> unreadCount.toString()
                    else -> "•"
                },
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
            )
        }
    }
}

@Composable
fun ContentRow(
    unreadCount: Int,
    reactionCount: Int,
    mentionCount: Int,
    lastMessage: LastMessageData?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val isUnreadBox =
            unreadCount > 0 ||
                    reactionCount > 0 ||
                    mentionCount > 0

        if(lastMessage != null) {
            LastMsgContent(
                Modifier.weight(if(isUnreadBox) 0.8f else 1f),
                lastMessage
            )
        }

        if(isUnreadBox) {
            UnreadBox(
                Modifier.weight(0.15f),
                unreadCount,
                reactionCount
            )
        }
    }
}