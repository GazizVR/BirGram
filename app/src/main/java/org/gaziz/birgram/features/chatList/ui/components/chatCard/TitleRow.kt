package org.gaziz.birgram.features.chatList.ui.components.chatCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.features.chatList.domain.model.LastMsgData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.formatChatCard(locale: Locale = Locale.getDefault()): String {
    val now = LocalDate.now()
    val date = toLocalDate()

    return when {
        date == now -> format(DateTimeFormatter.ofPattern("HH:mm"))

        date.isAfter(now.minusDays(7)) -> format(DateTimeFormatter.ofPattern("EEE", locale))

        date.year == now.year -> format(DateTimeFormatter.ofPattern("d MMM", locale))

        else -> format(DateTimeFormatter.ofPattern("dd.MM.yy"))
    }
}

@Composable
fun CardText(
    text: String,
    alpha: Float = 0.5f,
    textAlign: TextAlign = TextAlign.Center,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha)
){
    Text(
        text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontSize = 10.sp,
        maxLines = 1,
        textAlign = textAlign,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
fun TitleRow(
    chatTitle: String,
    lastMessage: LastMsgData?
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier.weight(1f)
        ) {
            CardText(text = chatTitle, alpha = 1f)
        }
        if(lastMessage != null) {
            CardText(
                lastMessage.date.formatChatCard(),
                0.35f,
                TextAlign.End
            )
        }
    }
}