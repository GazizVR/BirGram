package org.gaziz.birgram.presentation.chatList.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.domain.model.chat.ChatData
import org.gaziz.birgram.presentation.chatList.components.chatCard.ContentRow
import org.gaziz.birgram.presentation.chatList.components.chatCard.TitleRow
import org.gaziz.birgram.presentation.common.CardPhoto

@Composable
fun ChatCard(
    chatData: ChatData,
    downloadPhoto: (Int) -> Unit
) {
    val containerHeight = 80.dp
    val iconSize = 60.dp
    Card(
        shape = RoundedCornerShape(0),
        onClick = {},
        modifier = Modifier.height(containerHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CardPhoto(
                photo = chatData.photo,
                chatTitle = chatData.title,
                iconSize = iconSize,
                downloadPhoto = downloadPhoto
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                TitleRow(
                    chatData.title,
                    chatData.lastMessage
                )
                ContentRow(
                    chatData.unreadCount,
                    chatData.reactionCount,
                    chatData.mentionCount,
                    chatData.lastMessage
                )
            }
        }
    }
}