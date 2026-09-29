package org.gaziz.birgram.feature.chat.ui.component.messagecontent

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
import org.gaziz.birgram.core.telegram.api.model.message.MessageSendingState
import org.gaziz.birgram.core.telegram.ui.model.MessageSenderUiState
import org.gaziz.birgram.feature.chat.R
import org.gaziz.birgram.feature.chat.ui.ChatViewModel
import org.gaziz.birgram.feature.chat.ui.model.MessageContentUiState
import org.gaziz.birgram.feature.chat.ui.util.getUriForFile
import java.io.File
import java.util.Locale

fun Int.toDurationStr(): String {
    val totalSeconds = if (this < 0) 0 else this
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }
}

@Composable
fun ContentPreview(
    msgId: Long,
    content: MessageContentUiState,
    dateStr: String,
    fontSize: TextUnit,
    containerColor: Color,
    senderInfo: MessageSenderUiState?,
    sendingState: MessageSendingState?,
    originSenderTitle: String?,
    isOutgoing: Boolean
) {
    val viewModel = hiltViewModel<ChatViewModel>()
    val dateFontSize = 5.sp
    val context = LocalContext.current
    val onVideoClick: (File) -> Unit = {
        viewModel.setMediaId(msgId)
        val uri = context.getUriForFile(it)
        viewModel.setPlayerMedia(uri)
    }
    when(content){
        is MessageContentUiState.Text -> {
            TextPreview(
                text = content.text,
                dateStr = dateStr,
                containerColor = containerColor,
                senderInfo = senderInfo,
                sendingState = sendingState,
                originSenderTitle = originSenderTitle,
                isOutgoing = isOutgoing,
            )
        }
        is MessageContentUiState.Sticker -> {
            val mediaId by viewModel.mediaId.collectAsState()
            StickerPreview(
                modifier = Modifier.size(150.dp),
                content = content.content,
                date = dateStr,
                datePadding = 8.dp,
                fontSize = dateFontSize,
                player = viewModel.player,
                isCurrentMedia = mediaId == msgId,
                onVideoClick = onVideoClick,
                originSenderTitle = originSenderTitle
            )
        }
        is MessageContentUiState.AnimatedEmoji -> {
            if(content.content != null) {
                StickerPreview(
                    modifier = Modifier.size(100.dp),
                    content = content.content,
                    date = dateStr,
                    fontSize = dateFontSize,
                    originSenderTitle = originSenderTitle
                )
            } else {
                TextPreview(
                    text = content.emoji,
                    dateStr = dateStr,
                    containerColor = containerColor,
                    senderInfo = senderInfo,
                    sendingState = sendingState,
                    originSenderTitle = originSenderTitle,
                    isOutgoing = isOutgoing
                )
            }
        }
        is MessageContentUiState.Animation -> {
            val mediaId by viewModel.mediaId.collectAsState()
            MediaPreview(
                content = content.content,
                caption = content.caption,
                width = content.width,
                height = content.height,
                containerColor = containerColor,
                dateStr = dateStr,
                fontSize = dateFontSize,
                senderInfo = senderInfo,
                player = viewModel.player,
                isCurrentMedia = mediaId == msgId,
                onVideoClick = onVideoClick,
                sendingState = sendingState,
                originSenderTitle = originSenderTitle,
                isOutgoing = isOutgoing,
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
        is MessageContentUiState.Photo -> {
            MediaPreview(
                content = content.content,
                caption = content.caption,
                width = content.width,
                height = content.height,
                containerColor = containerColor,
                dateStr = dateStr,
                fontSize = dateFontSize,
                senderInfo = senderInfo,
                sendingState = sendingState,
                originSenderTitle = originSenderTitle,
                isOutgoing = isOutgoing
            )
        }
        is MessageContentUiState.Document -> {
            DocumentPreview(
                document = content,
                containerColor = containerColor,
                date = dateStr,
                originSenderTitle = originSenderTitle,
                isOutgoing = isOutgoing
            )
        }
        is MessageContentUiState.Video -> {
            val mediaId by viewModel.mediaId.collectAsState()
            MediaPreview(
                content = content.content,
                caption = content.caption,
                width = content.width,
                height = content.height,
                containerColor = containerColor,
                dateStr = dateStr,
                fontSize = dateFontSize,
                senderInfo = senderInfo,
                player = viewModel.player,
                isCurrentMedia = mediaId == msgId,
                onVideoClick = onVideoClick,
                onDispose = viewModel::removePlayerMedia,
                overlay = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.background.copy(0.5f),
                                    RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = if(mediaId == msgId) {
                                    val mediaPosition by viewModel.mediaPosition.collectAsState()
                                    mediaPosition.toDurationStr()
                                } else {
                                    content.duration.toDurationStr()
                                },
                                modifier = Modifier.padding(
                                    vertical = 2.dp,
                                    horizontal = 4.dp
                                ),
                                fontSize = fontSize,
                                lineHeight = fontSize,
                                maxLines = 1
                            )
                        }
                    }
                },
                sendingState = sendingState,
                originSenderTitle = originSenderTitle,
                isOutgoing = isOutgoing
            )
        }
        is MessageContentUiState.UnSupported -> {
            val unsupportedMessage = stringResource(R.string.unsupported_message)
            TextPreview(
                text = unsupportedMessage,
                dateStr = dateStr,
                containerColor = containerColor,
                senderInfo = senderInfo,
                sendingState = sendingState,
                originSenderTitle = originSenderTitle,
                isOutgoing = isOutgoing
            )
        }
    }
}
