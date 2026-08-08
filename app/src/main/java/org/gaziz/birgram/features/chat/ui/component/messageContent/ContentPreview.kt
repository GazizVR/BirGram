package org.gaziz.birgram.features.chat.ui.component.messageContent

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.MessageSenderInfo
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo

@Composable
fun ContentPreview(
    content: MessageContentInfo,
    date: String,
    fontSize: TextUnit,
    containerColor: Color,
    sender: MessageSenderInfo?
) {
    val dateFontSize = 5.sp
    when(content){
        is MessageContentInfo.Text -> {
            TextPreview(
                text = content.text,
                date = date,
                fontSize = fontSize,
                containerColor = containerColor,
                senderInfo = sender
            )
        }
        is MessageContentInfo.Sticker -> {
            StickerPreview(
                modifier = Modifier.size(150.dp),
                content = content.content,
                date = date,
                datePadding = 8.dp,
                fontSize = dateFontSize,
                containerColor = containerColor
            )
        }
        is MessageContentInfo.AnimatedEmoji -> {
            if(content.content != null) {
                StickerPreview(
                    modifier = Modifier.size(100.dp),
                    content = content.content,
                    date = date,
                    fontSize = dateFontSize,
                    containerColor = containerColor
                )
            } else {
                TextPreview(
                    text = content.emoji,
                    date = date,
                    fontSize = fontSize,
                    containerColor = containerColor,
                    senderInfo = sender
                )
            }
        }
        is MessageContentInfo.Animation -> {
            MediaPreview(
                content = content.content,
                caption = content.caption,
                width = content.width,
                height = content.height,
                containerColor = containerColor,
                date = date,
                fontSize = dateFontSize
            )
        }
        is MessageContentInfo.Photo -> {
            MediaPreview(
                content = content.content,
                caption = content.caption,
                width = content.width,
                height = content.height,
                containerColor = containerColor,
                date = date,
                fontSize = dateFontSize
            )
        }
        is MessageContentInfo.UnSupported -> {
            val unsupportedMessage = stringResource(R.string.unsupported_message)
            TextPreview(
                text = unsupportedMessage,
                date = date,
                fontSize = fontSize,
                containerColor = containerColor,
                senderInfo = sender
            )
        }
    }
}
