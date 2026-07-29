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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            if(showAuthor) {
                if(message.sender.avatar != null) {
                    ChatAvatar(
                        modifier = Modifier.size(40.dp),
                        avatar = message.sender.avatar,
                        placeHolderFontSize = 10.sp,
                    )
                } else {
                    Box(Modifier.size(40.dp))
                }
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
                        if(message.sender.name != null) {
                            Text(
                                text = message.sender.name,
                                color = message.sender.accentColor,
                                fontSize = fontSize,
                                lineHeight = fontSize
                            )
                        }
                    }
                    Box {
                        MessageContentPreview(
                            content = message.content,
                            fontSize = fontSize
                        )
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