package org.gaziz.birgram.presentation.chatList.components.chatCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R
import org.gaziz.birgram.domain.model.chat.LastMessageContent
import org.gaziz.birgram.domain.model.chat.LastMessageData

@Composable
fun LastMsgContent(
    modifier: Modifier,
    lastMessage: LastMessageData,
) {
    val msgType = stringArrayResource(R.array.message_type)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when(val cnt = lastMessage.content) {
            is LastMessageContent.Other -> CardText(cnt.type.trim())
            is LastMessageContent.Text -> CardText(cnt.text.trim())
            is LastMessageContent.Photo -> {
                if(cnt.miniThumbNail != null) {
                    AsyncImage(
                        model = cnt.miniThumbNail,
                        contentDescription = null,
                        modifier = Modifier.size(25.dp)
                    )
                }
                if(cnt.caption.isNotBlank()) {
                    CardText(cnt.caption.trim())
                } else {
                    CardText(msgType[0],color = MaterialTheme.colorScheme.primary)
                }
            }
            is LastMessageContent.Document -> {
                var text = "\uD83D\uDCC4 ${cnt.fileName}"
                if(cnt.caption.isNotBlank()) {
                    text = "\uD83D\uDCC4 ${cnt.caption.trim()}"
                }
                CardText(text, color = MaterialTheme.colorScheme.primary)
            }
            is LastMessageContent.Audio -> {
                var text = "🎵 ${cnt.fileName}"
                if(cnt.caption.isNotBlank()) {
                    text = "🎵 ${cnt.caption.trim()}"
                }
                CardText(text, color = MaterialTheme.colorScheme.primary)
            }
            is LastMessageContent.Video -> {
                var text = "🎥 ${cnt.fileName}"
                if(cnt.caption.isNotBlank()) {
                    text = "🎥 ${cnt.caption.trim()}"
                }
                CardText(text, color = MaterialTheme.colorScheme.primary)
            }
            is LastMessageContent.Sticker -> {
               CardText( "${cnt.emoji} ${msgType[1]}", color = MaterialTheme.colorScheme.primary)
            }
            is LastMessageContent.VoiceNote -> {
                CardText( msgType[2], color = MaterialTheme.colorScheme.primary)
            }
            is LastMessageContent.VideoNote -> {
                CardText( msgType[3], color = MaterialTheme.colorScheme.primary)
            }
            is LastMessageContent.GIF -> {
                CardText( msgType[4], color = MaterialTheme.colorScheme.primary)
            }
            is LastMessageContent.Draft -> {
                CardText( msgType[5], color = MaterialTheme.colorScheme.error)
            }
        }
    }
}