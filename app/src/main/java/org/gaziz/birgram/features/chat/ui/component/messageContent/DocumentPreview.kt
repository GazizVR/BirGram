package org.gaziz.birgram.features.chat.ui.component.messageContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo

@Composable
fun DocumentPreview(
    document: MessageContentInfo.Document,
    containerColor: Color,
    fontSize: TextUnit,
    date: String
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(containerColor,shape)
    ) {
        Text(
            text = date,
            fontSize = fontSize,
            lineHeight = fontSize
        )
    }
}