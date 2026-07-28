package org.gaziz.birgram.features.chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import org.gaziz.birgram.core.ui.component.ChatAvatar
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
    val showAuthor =
        message.sender != null &&
        !message.isOutgoing
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
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if(
                showAuthor &&
                message.sender.avatar != null
            ) {
                ChatAvatar(
                    modifier = Modifier.size(40.dp),
                    avatar = message.sender.avatar,
                    placeHolderFontSize = 10.sp,
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(containerColor),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(9.dp)
                ) {
                    if(showAuthor) {
                        Text(
                            text = message.sender.name,
                            color = message.sender.accentColor,
                            fontSize = fontSize,
                            lineHeight = fontSize
                        )
                    }
                    Box {
                        when(val cnt = message.content){
                            is MessageContent.Text -> {
                                SelectionContainer {
                                    Row {
                                        Text(
                                            text = cnt.text,
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
                            else -> {
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
    }
}