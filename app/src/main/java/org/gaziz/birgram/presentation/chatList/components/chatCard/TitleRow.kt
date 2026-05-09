package org.gaziz.birgram.presentation.chatList.components.chatCard

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
import org.gaziz.birgram.domain.model.chat.LastMessageData

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
    lastMessage: LastMessageData?
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
                lastMessage.date,
                0.35f,
                TextAlign.End
            )
        }
    }
}