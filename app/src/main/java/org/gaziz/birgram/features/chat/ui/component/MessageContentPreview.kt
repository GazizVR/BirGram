package org.gaziz.birgram.features.chat.ui.component

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
fun MessageContentPreview(
    content: MessageContentInfo,
    date: String,
    fontSize: TextUnit,
    containerColor: Color,
    sender: MessageSenderInfo?
) {
    when(content){
        is MessageContentInfo.Text -> {
            MessageTextPreview(
                text = content.text,
                date = date,
                fontSize = fontSize,
                containerColor = containerColor,
                senderInfo = sender
            )
        }
        is MessageContentInfo.Sticker -> {
            MessageStickerPreview(
                modifier = Modifier.size(150.dp),
                content = content.content,
                date = date,
                datePadding = 8.dp,
                fontSize = 5.sp,
                containerColor = containerColor
            )
        }
        is MessageContentInfo.AnimatedEmoji -> {
            if(content.content != null) {
                MessageStickerPreview(
                    modifier = Modifier.size(100.dp),
                    content = content.content,
                    date = date,
                    fontSize = 5.sp,
                    containerColor = containerColor
                )
            } else {
                MessageTextPreview(
                    text = content.emoji,
                    date = date,
                    fontSize = fontSize,
                    containerColor = containerColor,
                    senderInfo = sender
                )
            }
        }
        is MessageContentInfo.UnSupported -> {
            val unsupportedMessage = stringResource(R.string.unsupported_message)
            MessageTextPreview(
                text = unsupportedMessage,
                date = date,
                fontSize = fontSize,
                containerColor = containerColor,
                senderInfo = sender
            )
        }
    }
}
