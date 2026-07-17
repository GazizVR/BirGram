package org.gaziz.birgram.features.chatList.ui.components.chatCard

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
import org.gaziz.birgram.features.chatList.domain.model.LastMsgContent
import org.gaziz.birgram.features.chatList.domain.model.LastMsgData

@Composable
fun LastMsgContent(
    modifier: Modifier,
    lastMessage: LastMsgData,
) {
    val msgType = stringArrayResource(R.array.message_type)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when(val cnt = lastMessage.content) {
            is LastMsgContent.Other -> CardText(msgType[5])
            is LastMsgContent.Text -> CardText(cnt.text.trim())
            is LastMsgContent.Photo -> {
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
            is LastMsgContent.Document -> {
                var text = "\uD83D\uDCC4 ${cnt.fileName}"
                if(cnt.caption.isNotBlank()) {
                    text = "\uD83D\uDCC4 ${cnt.caption.trim()}"
                }
                CardText(text, color = MaterialTheme.colorScheme.primary)
            }
            is LastMsgContent.Audio -> {
                var text = "🎵 ${cnt.fileName}"
                if(cnt.caption.isNotBlank()) {
                    text = "🎵 ${cnt.caption.trim()}"
                }
                CardText(text, color = MaterialTheme.colorScheme.primary)
            }
            is LastMsgContent.Video -> {
                var text = "🎥 ${cnt.fileName}"
                if(cnt.caption.isNotBlank()) {
                    text = "🎥 ${cnt.caption.trim()}"
                }
                CardText(text, color = MaterialTheme.colorScheme.primary)
            }
            is LastMsgContent.Sticker -> {
               CardText( "${cnt.emoji} ${msgType[1]}", color = MaterialTheme.colorScheme.primary)
            }
            is LastMsgContent.VoiceNote -> {
                CardText( msgType[2], color = MaterialTheme.colorScheme.primary)
            }
            is LastMsgContent.VideoNote -> {
                CardText( msgType[3], color = MaterialTheme.colorScheme.primary)
            }
            is LastMsgContent.GIF -> {
                CardText( msgType[4], color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}