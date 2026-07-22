package org.gaziz.birgram.features.chatList.ui.component.chatCard

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent

@Composable
fun DraftMessagePreview(
    modifier: Modifier,
    draftMessage: DraftMessage,
    fontSize: TextUnit
) {
    val draftMsgPlaceHolder = stringResource(R.string.draft_message)
    Row(
        modifier = modifier
    ){
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                    append("$draftMsgPlaceHolder ")
                }
                if (
                    draftMessage.content is DraftMessageContent.Text
                ) {
                    append(draftMessage.content.text)
                }
            },
            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
            fontSize = fontSize,
            lineHeight = fontSize,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}