package org.gaziz.birgram.features.searchChats.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.core.telegram.domain.model.UserStatus
import org.gaziz.birgram.features.searchChats.domain.model.ChatData
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatType
import org.gaziz.birgram.core.ui.components.ChatAvatar
import org.gaziz.birgram.core.ui.components.ChatTypeCnt

@Composable
fun SearchChatCard(
    chatData: ChatData,
    downloadPhoto: (Int) -> Unit,
    onClick: (Long) -> Unit
) {
    val containerHeight = 80.dp
    val iconSize = 60.dp
    val cardColor = CardDefaults.cardColors().containerColor
    Card(
        shape = RoundedCornerShape(0),
        onClick = { onClick(chatData.id) },
        modifier = Modifier.height(containerHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ChatAvatar(
                photo = chatData.photo,
                chatTitle = chatData.title,
                iconSize = iconSize,
                downloadPhoto = downloadPhoto,
                isOnline = if(chatData.type is ChatType.Private) chatData.type.userStatus is UserStatus.Online else false,
                parentColor = cardColor
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chatData.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                ChatTypeCnt(chatData.type)
            }
        }
    }
}