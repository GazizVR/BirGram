package org.gaziz.birgram.features.chat.ui.component.messageContent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
    val isDownloading by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .clip(shape)
            .background(containerColor,shape)
    ) {
        Row (
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) {

                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = containerColor,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }
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
                    Spacer(Modifier.height(4.dp))
                    var text = ""
                    if(document.size != null) text += document.size
                    if(document.type != null) text += " ${document.type}"
                    Box {
                        Row {
                            Text(
                                text = text,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = fontSize,
                                lineHeight = fontSize,
                                fontWeight = FontWeight.Thin,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "24:35",
                                color = Color.Transparent,
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
        Text(
            text = date,
            color = MaterialTheme.colorScheme.onBackground.copy(0.5f),
            fontSize = 5.sp,
            lineHeight = 5.sp,
            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp),
            maxLines = 1
        )
    }
}