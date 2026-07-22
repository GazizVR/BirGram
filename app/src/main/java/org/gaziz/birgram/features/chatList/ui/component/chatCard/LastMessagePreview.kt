package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent

@Composable
fun LastMsgText(
    text: String,
    color: Color,
    fontSize: TextUnit
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        lineHeight = fontSize,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
fun LastMsgMedia(
    media: Any?,
    caption: String,
    captionPlaceHolder: String,
    fontSize: TextUnit
) {
    if(media != null) {
        AsyncImage(
            model = media,
            contentDescription = null,
        )
        Spacer(Modifier.width(4.dp))
    }
    LastMsgText(
        text = caption.ifBlank { captionPlaceHolder },
        color = if(caption.isNotBlank()) {
            MaterialTheme.colorScheme.onBackground.copy(0.5f)
        } else {
            MaterialTheme.colorScheme.primary
        },
        fontSize = fontSize
    )
}

@Composable
fun LastMessagePreview(
    modifier: Modifier,
    lastMessage: Message,
    fontSize: TextUnit,
    sender: String? = null
) {
    val msgContents = stringArrayResource(R.array.message_contents)
    Row(
        modifier = modifier
    ) {
        if(sender != null) {
            LastMsgText(
                text = "$sender: ",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = fontSize,
            )
        }
        when (val cnt = lastMessage.content) {
            is MessageContent.Text -> {
                LastMsgText(
                    text = cnt.text,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontSize = fontSize,
                )
            }

            is MessageContent.Sticker -> {
                LastMsgText(
                    text = "${cnt.emoji} ${msgContents[0]}",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = fontSize
                )
            }

            is MessageContent.GIF -> {
                LastMsgMedia(
                    media = cnt.miniThumbnail,
                    caption = cnt.caption,
                    captionPlaceHolder = msgContents[1],
                    fontSize = fontSize
                )
            }

            is MessageContent.Photo -> {
                LastMsgMedia(
                    media = cnt.miniThumbnail,
                    caption = cnt.caption,
                    captionPlaceHolder = msgContents[2],
                    fontSize = fontSize
                )
            }

            else -> {
                val unsupportedMessage = stringResource(R.string.unsupported_message)
                LastMsgText(
                    text = unsupportedMessage,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontSize = fontSize,
                )
            }
        }
    }
}