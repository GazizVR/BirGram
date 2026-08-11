package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.component.ChatAvatar
import org.gaziz.birgram.features.chat.ui.component.messageContent.ContentPreview
import org.gaziz.birgram.features.chat.ui.model.MessageUiState

@Composable
fun MessageCard(
    message: MessageUiState,
    fontSize: TextUnit
) {
    val containerColor = if(message.isOutgoing){
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }
    val spacerSize = 40.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if(message.isOutgoing) Arrangement.End else Arrangement.Start
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if(
                message.sender != null &&
                !message.isOutgoing
            ) {
                if(message.sender.avatar != null) {
                    ChatAvatar(
                        modifier = Modifier.size(spacerSize),
                        avatar = message.sender.avatar,
                        placeHolderFontSize = 10.sp,
                    )
                } else {
                    Spacer(Modifier.width(spacerSize))
                }
            } else {
                if(message.isOutgoing) {
                    Spacer(Modifier.width(spacerSize))
                }
            }
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = if(message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                ContentPreview(
                    msgId = message.id,
                    content = message.content,
                    fontSize = fontSize,
                    date = message.date,
                    containerColor = containerColor,
                    sender = if (!message.isOutgoing) message.sender else null
                )
            }
            if(!message.isOutgoing) {
                Spacer(Modifier.width(spacerSize))
            }
        }
    }
}