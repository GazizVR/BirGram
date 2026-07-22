package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent

@Composable
fun LastMessagePreview(
    modifier: Modifier,
    lastMessage: Message,
    fontSize: TextUnit
) {
    Row(
        modifier = modifier
    ) {
        when (val msgCnt = lastMessage.content) {
            is MessageContent.Text -> {
                Text(
                    text = msgCnt.text,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontSize = fontSize,
                    lineHeight = fontSize,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

            else -> {
                val unsupportedMessage = stringResource(R.string.unsupported_message)
                Text(
                    text = unsupportedMessage,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontSize = fontSize,
                    lineHeight = fontSize,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}