package org.gaziz.birgram.features.chat.ui.component.messageContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo

@Composable
fun DocumentPreview(
    document: MessageContentInfo.Document,
    containerColor: Color,
    date: String
) {
    val fontSize = 6.sp
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = Modifier
            .clip(shape)
            .background(containerColor,shape)
    ) {
        Row (
            modifier = Modifier.padding(8.dp)
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ){
                if(document.fileName != null) {
                    Text(
                        text = document.fileName,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = fontSize,
                        lineHeight = fontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )
                }
                if(
                    document.size != null ||
                    document.type != null
                ) {
                    Spacer(Modifier.height(3.dp))
                    var text = ""
                    if(document.size != null) text += document.size
                    if(document.type != null) text += " ${document.type}"
                    Text(
                        text = text,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = fontSize,
                        lineHeight = fontSize,
                        fontWeight = FontWeight.Thin,
                        maxLines = 1
                    )
                }
                Row {
                    if(
                        document.size != null ||
                        document.type != null ||
                        document.fileName != null
                    ) {
                        Spacer(Modifier.weight(1f))
                    }
                    Text(
                        text = date,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                        fontSize = fontSize,
                        lineHeight = fontSize,
                        fontWeight = FontWeight.Thin,
                        maxLines = 1
                    )
                }
            }
        }
    }
}