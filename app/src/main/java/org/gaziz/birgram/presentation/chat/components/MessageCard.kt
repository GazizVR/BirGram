package org.gaziz.birgram.presentation.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.data.mapper.formatForChatList
import org.gaziz.birgram.domain.model.message.MessageContent
import org.gaziz.birgram.domain.model.message.MessageData

@Composable
fun MessageCard(
    msg: MessageData
) {
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
           Row(
               modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
               horizontalArrangement = Arrangement.spacedBy(4.dp),
           ) {
               Text(
                   if(msg.content is MessageContent.Text) msg.content.text else msg.content.toString(),
                   style = MaterialTheme.typography.labelSmall,
                   fontSize = 10.sp,
                   textAlign = TextAlign.Start,
                   overflow = TextOverflow.Ellipsis,
               )
               Text(
                   msg.date.formatForChatList(),
                   style = MaterialTheme.typography.labelSmall,
                   color = MaterialTheme.colorScheme.onBackground.copy(0.75f),
                   fontSize = 8.sp,
                   maxLines = 1,
                   textAlign = TextAlign.End,
                   overflow = TextOverflow.Ellipsis,
               )
           }
        }
    }
}