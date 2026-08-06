package org.gaziz.birgram.features.chat.ui.component

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.core.telegram.api.model.message.MessageSenderInfo

@Composable
fun MessageTextPreview(
    text: String,
    date: String,
    fontSize: TextUnit,
    containerColor: Color,
    senderInfo: MessageSenderInfo?
) {
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
            if(senderInfo != null) {
                if(senderInfo.name != null) {
                    Text(
                        text = senderInfo.name,
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
                Text(
                    text = date,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    fontSize = 5.sp,
                    lineHeight = 5.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}