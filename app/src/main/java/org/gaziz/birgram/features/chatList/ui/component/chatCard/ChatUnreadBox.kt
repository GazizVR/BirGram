package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.features.chatList.ui.model.UnreadBadgeUiState

@Composable
fun ChatUnreadBadge(
    unreadBadge: UnreadBadgeUiState,
) {
    if(
        unreadBadge.unreadCount > 0 ||
        unreadBadge.mentionCount > 0 ||
        unreadBadge.reactionCount > 0
    ) {
        Row {
            if(unreadBadge.reactionCount > 0) {
                Text(
                    text = "❤\uFE0F",
                    fontSize = unreadBadge.fontSize*1.35,
                    lineHeight = unreadBadge.fontSize*1.35,
                )
            }
            Spacer(Modifier.width(3.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground.copy(0.35f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (unreadBadge.mentionCount+unreadBadge.unreadCount).toString(),
                    fontSize = unreadBadge.fontSize,
                    lineHeight = unreadBadge.fontSize,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(
                        horizontal = 5.dp,
                        vertical = 3.dp
                    ),
                    maxLines = 1
                )
            }
        }
    }
}
