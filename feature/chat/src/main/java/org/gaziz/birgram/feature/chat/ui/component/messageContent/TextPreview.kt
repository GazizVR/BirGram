package org.gaziz.birgram.feature.chat.ui.component.messageContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.model.MessageSenderInfo
import org.gaziz.birgram.core.ui.theme.BirGramTheme
import org.gaziz.telegram.api.model.message.SendingState

@Composable
fun TextPreview(
    text: String,
    isSpacer: Boolean = false,
    date: String,
    fontSize: TextUnit,
    containerColor: Color,
    senderInfo: MessageSenderInfo?,
    sendingState: SendingState?
) {
    var isSingleLine by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(9.dp)
        ) {
            if(senderInfo != null) {
                if(senderInfo.name != null) {
                    Text(
                        text = senderInfo.name!!,
                        color = senderInfo.accentColor,
                        fontSize = fontSize,
                        lineHeight = fontSize
                    )
                }
            }
            Box {
                SelectionContainer {
                    Row {
                        Text(
                            text = text,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = fontSize,
                            lineHeight = fontSize,
                            modifier = if(isSpacer) Modifier.weight(1f) else Modifier,
                            onTextLayout = {
                                isSingleLine = it.lineCount < 2
                            }
                        )
                        DisableSelection {
                            Text(
                                text = " $date",
                                color = Color.Transparent,
                                fontSize = 5.sp,
                                lineHeight = 5.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
                Text(
                    text = date,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontSize = 5.sp,
                    lineHeight = 5.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(
                        if(isSingleLine) {
                            Alignment.CenterEnd
                        } else {
                            Alignment.BottomEnd
                        }
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun ThisPreview() {
    BirGramTheme {
        TextPreview(
            text = "Message",
            isSpacer = false,
            date = "09:53",
            fontSize = 6.sp,
            containerColor = MaterialTheme.colorScheme.primary,
            senderInfo = MessageSenderInfo(
                name = "User",
                avatar = null,
                accentColor = Color.Red
            ),
            sendingState = SendingState.Pending
        )
    }
}