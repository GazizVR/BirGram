package org.gaziz.birgram.feature.chat.ui.component.messageContent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.ui.icon.clock
import org.gaziz.birgram.core.ui.icon.error
import org.gaziz.birgram.core.ui.model.MessageSenderInfo
import org.gaziz.birgram.core.ui.theme.BirGramTheme
import org.gaziz.telegram.api.model.message.SendingState

@Composable
fun SendingStatePreview(
    modifier: Modifier = Modifier,
    sendingState: SendingState,
    color: Color? = null
) {
    when (sendingState) {
        is SendingState.Pending -> {
            Icon(
                imageVector = clock,
                contentDescription = null,
                modifier = modifier,
                tint = color ?: MaterialTheme.colorScheme.secondary
            )
        }
        is SendingState.Failed -> {
            Icon(
                imageVector = error,
                contentDescription = null,
                modifier = modifier,
                tint = color ?: MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun TextPreview(
    text: String,
    fontSize: TextUnit = 6.sp,
    dateStr: String,
    dateFontSize: TextUnit = (fontSize.value-1.sp.value).sp,
    containerColor: Color,
    senderInfo: MessageSenderInfo?,
    sendingState: SendingState?,
    isSpacer: Boolean = false
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(
                vertical = 6.dp,
                horizontal = 10.dp
            )
        ) {
            if(senderInfo?.name != null) {
                Text(
                    text = senderInfo.name!!,
                    color = senderInfo.accentColor,
                    fontSize = fontSize,
                    lineHeight = fontSize,
                    maxLines = 1
                )
            }
            Box(contentAlignment = Alignment.Center) {
                val stateModifier = Modifier.size(10.dp)
                FlowRow {
                    SelectionContainer(
                        modifier = if(isSpacer) Modifier.weight(1f) else Modifier
                    ) {
                        Text(
                            text = text,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = fontSize,
                            lineHeight = fontSize
                        )
                    }
                    Text(
                        text = " $dateStr",
                        color = Color.Transparent,
                        fontSize = dateFontSize,
                        lineHeight = dateFontSize,
                        maxLines = 1
                    )
                    if(sendingState != null) {
                        Spacer(Modifier.width(2.dp))
                        SendingStatePreview(
                            modifier = stateModifier,
                            sendingState = sendingState,
                            color = Color.Transparent
                        )
                    }
                }
                Row(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = " $dateStr",
                        color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                        fontSize = dateFontSize,
                        lineHeight = dateFontSize,
                        maxLines = 1,
                    )
                    if(sendingState != null) {
                        SendingStatePreview(
                            modifier = stateModifier,
                            sendingState = sendingState
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TextPrevPreview() {
    BirGramTheme(true) {
        TextPreview(
            text = "hello",
            fontSize = 6.sp,
            dateStr = "21:32",
            dateFontSize = 5.sp,
            containerColor = MaterialTheme.colorScheme.primary,
            senderInfo = null,
            sendingState = SendingState.Pending,
            isSpacer = false
        )
    }
}