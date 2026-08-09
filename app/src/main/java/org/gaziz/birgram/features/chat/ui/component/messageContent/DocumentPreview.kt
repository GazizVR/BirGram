package org.gaziz.birgram.features.chat.ui.component.messageContent

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import org.gaziz.birgram.core.ui.icon.arrowDownwardAlt
import org.gaziz.birgram.core.ui.icon.fileOpen
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo
import java.io.File

fun getUriForFile(
    context: Context,
    file: File
): Uri {
    val authority = "${context.packageName}.fileProvider"
    return FileProvider.getUriForFile(context,authority,file)
}

@Composable
fun DocumentPreview(
    document: MessageContentInfo.Document,
    containerColor: Color,
    date: String
) {
    val fontSize = 6.sp
    val shape = RoundedCornerShape(16.dp)
    var isDownloading by rememberSaveable { mutableStateOf(false) }
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
            val context = LocalContext.current
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource
                    ) {
                        if(document.file != null) {
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(
                                    getUriForFile(context,document.file),
                                    document.mimeType ?: "*/*"
                                )
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent,"Open with"))
                        } else {
                            if(!isDownloading) {
                                isDownloading = true
                                document.downloadDocument()
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                val size = 20.dp
                when {
                    document.file != null -> {
                        Icon(
                            imageVector = fileOpen,
                            contentDescription = null,
                            tint = containerColor,
                            modifier = Modifier.size(size)
                        )
                    }
                    isDownloading -> {
                        CircularProgressIndicator(
                            color = containerColor,
                            modifier = Modifier.size(size),
                            strokeWidth = 2.dp
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = arrowDownwardAlt,
                            contentDescription = null,
                            tint = containerColor,
                            modifier = Modifier.size(size+5.dp)
                        )
                    }
                }
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