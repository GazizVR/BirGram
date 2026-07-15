package org.gaziz.birgram.features.chat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.R
import org.gaziz.birgram.features.chatList.domain.model.MessageContent
import org.gaziz.birgram.features.chat.domain.model.MessageData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.formatMessageCard(): String {
    return format(DateTimeFormatter.ofPattern("HH:mm"))
}

@Composable
fun MessageText(
    text: String,
    date: String
) {
    SelectionContainer {
        Box {
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontSize = 8.sp, color = MaterialTheme.colorScheme.onBackground)){
                        append(text)
                    }
                    withStyle(SpanStyle(fontSize = 7.sp, color = Color.Transparent)){
                        append(" 14:32")
                    }
                },
                style = MaterialTheme.typography.labelSmall,
                overflow = TextOverflow.Ellipsis,
            )
            DisableSelection {
                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.75f),
                    fontSize = 7.sp,
                    maxLines = 1,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Composable
fun MessageCard(
    msg: MessageData
) {
    val msgType = stringArrayResource(R.array.message_type)
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if(msg.isMy) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = if(msg.isMy) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
                contentColor = MaterialTheme.colorScheme.onBackground
            )
        ) {
           Row(Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
               when(msg.content) {
                   is MessageContent.Text -> { MessageText(msg.content.text,msg.date.formatMessageCard()) }
                   else -> { MessageText(msgType[6],msg.date.formatMessageCard()) }
               }
           }
        }
    }
}