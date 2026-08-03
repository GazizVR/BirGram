package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.R
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo

@Composable
fun MessageContentPreview(
    content: MessageContentInfo,
    fontSize: TextUnit
) {
    when(content){
        is MessageContentInfo.Text -> {
            SelectionContainer {
                Row {
                    Text(
                        text = content.text,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = fontSize,
                        lineHeight = fontSize
                    )
                    DisableSelection {
                        Text(
                            text = " 24:32",
                            color = Color.Transparent,
                            fontSize = 5.sp,
                            lineHeight = 5.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }
        is MessageContentInfo.Sticker -> {
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = content.emoji,
                    textAlign = TextAlign.Center
                )
            }
        }
        is MessageContentInfo.UnSupported -> {
            val unsupportedMessage = stringResource(R.string.unsupported_message)
            Row {
                Text(
                    text = unsupportedMessage,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = fontSize,
                    lineHeight = fontSize
                )
                Text(
                    text = " 24:32",
                    color = Color.Transparent,
                    fontSize = 5.sp,
                    lineHeight = 5.sp,
                    maxLines = 1
                )
            }
        }
    }
}
