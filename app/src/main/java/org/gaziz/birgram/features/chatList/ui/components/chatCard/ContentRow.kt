package org.gaziz.birgram.features.chatList.ui.components.chatCard

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.telegram.model.DraftMessage
import org.gaziz.birgram.features.chatList.domain.model.LastMsgData

@Composable
fun UnreadBox(
    unreadCount: Int,
    mentionCount: Int,
    reactionCount: Int
) {
    Box(contentAlignment = Alignment.CenterEnd) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = (if(mentionCount > 0) "@" else "") +
                    when {
                        reactionCount > 0 -> "❤\uFE0F"
                        unreadCount > 0 ->  unreadCount.toString()
                        else -> ""
                    },
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ContentRow(
    unreadCount: Int,
    reactionCount: Int,
    mentionCount: Int,
    lastMessage: LastMsgData?,
    draftMessage: DraftMessage?
) {
    val isUnreadBox = unreadCount > 0 || mentionCount > 0 || reactionCount > 0
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if(lastMessage != null || draftMessage != null) {
            LastMsgContent(
                Modifier.weight(1f),
                lastMessage!!,
                draftMessage
            )
        }

        if(isUnreadBox) {
            UnreadBox(
                unreadCount,
                mentionCount,
                reactionCount
            )
        }
    }
}