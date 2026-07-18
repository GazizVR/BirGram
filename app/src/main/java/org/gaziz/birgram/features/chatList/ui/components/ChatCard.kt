package org.gaziz.birgram.features.chatList.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.core.ui.components.ChatAvatar
import org.gaziz.birgram.core.telegram.chats.api.model.Chat
import org.gaziz.birgram.features.chatList.ui.components.chatCard.ContentRow
import org.gaziz.birgram.features.chatList.ui.components.chatCard.TitleRow

@Composable
fun ChatCard(
    chat: Chat,
    isOnline: Boolean,
    downloadPhoto: (Int) -> Unit,
    onClick: (Long) -> Unit
) {
    val containerHeight = 80.dp
    val iconSize = 60.dp
    val cardColor = CardDefaults.cardColors().containerColor
    Card(
        shape = RoundedCornerShape(0),
        onClick = { onClick(chat.id) },
        modifier = Modifier.height(containerHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatAvatar(
                photo = chat.photo,
                chatTitle = chat.title,
                iconSize = iconSize,
                downloadPhoto = downloadPhoto,
                isOnline = isOnline,
                parentColor = cardColor
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                TitleRow(
                    chat.title,
                    when {
                        chat.lastMessage != null -> chat.lastMessage.date
                        chat.draftMessage != null -> chat.draftMessage.date
                        else -> null
                    }
                )
                ContentRow(
                    chat.unreadCount,
                    chat.reactionCount,
                    chat.mentionCount,
                    chat.lastMessage,
                    chat.draftMessage
                )
            }
        }
    }
}