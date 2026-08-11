package org.gaziz.birgram.features.chat.ui.component.messageContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.message.MessageSenderInfo
import org.gaziz.birgram.features.chat.ui.ChatViewModel
import org.gaziz.birgram.features.chat.ui.mapper.getUriForFile
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo
import java.io.File

@Composable
fun ContentPreview(
    content: MessageContentInfo,
    date: String,
    fontSize: TextUnit,
    containerColor: Color,
    sender: MessageSenderInfo?
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val dateFontSize = 5.sp
    val context = LocalContext.current
    val onVideoClick: (File) -> Unit = {
        viewModel.setMedia(it)
        val uri = getUriForFile(context,it)
        viewModel.setPlayerMedia(uri)
    }
    when(content){
        is MessageContentInfo.Text -> {
            TextPreview(
                text = content.text,
                date = date,
                fontSize = fontSize,
                containerColor = containerColor,
                senderInfo = sender
            )
        }
        is MessageContentInfo.Sticker -> {
            val mediaFile by viewModel.mediaFile.collectAsState()
            StickerPreview(
                modifier = Modifier.size(150.dp),
                content = content.content,
                date = date,
                datePadding = 8.dp,
                fontSize = dateFontSize,
                player = viewModel.player,
                currentMedia = mediaFile,
                onVideoClick = onVideoClick
            )
        }
        is MessageContentInfo.AnimatedEmoji -> {
            if(content.content != null) {
                StickerPreview(
                    modifier = Modifier.size(100.dp),
                    content = content.content,
                    date = date,
                    fontSize = dateFontSize
                )
            } else {
                TextPreview(
                    text = content.emoji,
                    date = date,
                    fontSize = fontSize,
                    containerColor = containerColor,
                    senderInfo = sender
                )
            }
        }
        is MessageContentInfo.Animation -> {
            val mediaFile by viewModel.mediaFile.collectAsState()
            MediaPreview(
                content = content.content,
                caption = content.caption,
                width = content.width,
                height = content.height,
                containerColor = containerColor,
                date = date,
                fontSize = dateFontSize,
                player = viewModel.player,
                currentMedia = mediaFile,
                onVideoClick = onVideoClick
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.background.copy(0.35f),
                                RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "GIF",
                            modifier = Modifier.padding(
                                vertical = 2.dp,
                                horizontal = 4.dp
                            ),
                            fontSize = fontSize,
                            lineHeight = fontSize,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }
        is MessageContentInfo.Photo -> {
            MediaPreview(
                content = content.content,
                caption = content.caption,
                width = content.width,
                height = content.height,
                containerColor = containerColor,
                date = date,
                fontSize = dateFontSize
            )
        }
        is MessageContentInfo.Document -> {
            DocumentPreview(
                document = content,
                containerColor = containerColor,
                date = date,
            )
        }
        is MessageContentInfo.UnSupported -> {
            val unsupportedMessage = stringResource(R.string.unsupported_message)
            TextPreview(
                text = unsupportedMessage,
                date = date,
                fontSize = fontSize,
                containerColor = containerColor,
                senderInfo = sender
            )
        }
    }
}
