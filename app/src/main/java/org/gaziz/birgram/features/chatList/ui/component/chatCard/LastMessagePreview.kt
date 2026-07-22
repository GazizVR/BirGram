package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent

@Composable
fun LastMessagePreview(
    draftMessage: DraftMessage?,
    lastMessage: Message?,
    fontSize: TextUnit
) {
    if(draftMessage != null) {
        val draftMsgPlaceHolder = stringResource(R.string.draft_message)
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                    append("$draftMsgPlaceHolder ")
                }
                if(
                    draftMessage.content is DraftMessageContent.Text &&
                    !draftMessage.content.clearDraft
                ) {
                    append(draftMessage.content.text)
                }
            },
            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
            fontSize = fontSize,
            lineHeight = fontSize
        )
    } else {
        if(lastMessage != null) {
            when(val msgCnt = lastMessage.content) {
                is MessageContent.Text -> {
                    Text(
                        text = msgCnt.text,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                        fontSize = fontSize,
                        lineHeight = fontSize,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                else -> {
                    val unsupportedMessage = stringResource(R.string.unsupported_message)
                    Text(
                        text = unsupportedMessage,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                        fontSize = fontSize,
                        lineHeight = fontSize,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}