package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
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
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(containerColor),
        ) {
            Box(
                modifier = Modifier.padding(9.dp)
            ) {
                when(val cnt = message.content){
                    is MessageContent.Text -> {
                        Text(
                            text = buildAnnotatedString {
                                append(cnt.text)
                                withStyle(SpanStyle(color = Color.Transparent)) {
                                    append("14:32")
                                }
                            },
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = fontSize,
                            lineHeight = fontSize,
                        )
                    }
                    else -> {
                        val unsupportedMessage = stringResource(R.string.unsupported_message)
                        Text(
                            text = buildAnnotatedString {
                                append(unsupportedMessage)
                                withStyle(SpanStyle(color = Color.Transparent)) {
                                    append(" 14:32")
                                }
                            },
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = fontSize,
                            lineHeight = fontSize
                        )
                    }
                }
                Text(
                    text = message.date,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontSize = 5.sp,
                    lineHeight = 5.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}