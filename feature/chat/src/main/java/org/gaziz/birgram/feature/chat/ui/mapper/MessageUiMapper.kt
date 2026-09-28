package org.gaziz.birgram.feature.chat.ui.mapper

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import org.gaziz.birgram.core.telegram.api.model.message.MessageOrigin
import org.gaziz.birgram.core.telegram.api.model.message.MessageProperties
import org.gaziz.birgram.core.telegram.api.model.message.MessageSender
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.api.usecase.DownloadMessageMedia
import org.gaziz.birgram.core.ui.model.MessageSenderUiState
import org.gaziz.birgram.feature.chat.domain.usecase.GetPhotoBySizes
import org.gaziz.birgram.feature.chat.ui.model.MediaUiState
import org.gaziz.birgram.feature.chat.ui.model.MessageContentUiState
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState
import java.io.File
import javax.inject.Inject

class MessageUiMapper @Inject constructor(
    private val getPhotoBySizes: GetPhotoBySizes,
    private val downloadMessageMedia: DownloadMessageMedia,
) {
    fun map(
        msg: Message,
        prevMsg: Message? = null,
        nextMsg: Message? = null,
        senderInfo: MessageSenderUiState? = null,
        chatById: Map<Long, Chat>,
        userById: Map<Long, User>,
        propertiesById: Map<Long, MessageProperties>
    ): MessageUiState {
        val chat = chatById[msg.chatId]
        val sender = run {
            var newData = senderInfo?.copy(name = null, avatar = null)
            if(prevMsg != null) {
                if(msg.sender != prevMsg.sender) {
                    newData = newData?.copy(name = senderInfo?.name)
                }
            } else {
                newData = newData?.copy(name = senderInfo?.name)
            }
            if(nextMsg != null) {
                if(msg.sender != nextMsg.sender) {
                    newData = newData?.copy(avatar = senderInfo?.avatar)
                }
            } else {
                newData = newData?.copy(avatar = senderInfo?.avatar)
            }
            if(chat?.type is ChatType.Private || chat?.type is ChatType.Secret) {
                newData = null
            }
            if(
                msg.sender is MessageSender.Chat &&
                (msg.sender as MessageSender.Chat).id == msg.chatId
            ) {
                newData = null
            }
            newData
        }
        val msgContent = when(val cnt = msg.content) {
            is MessageContent.Photo -> {
                val photoSize = getPhotoBySizes(cnt.sizes)
                var width = 150
                var heigh = 150
                var content: MediaUiState? = null
                if(cnt.miniThumbnail != null) {
                    val bitmap = BitmapFactory.decodeByteArray(
                        cnt.miniThumbnail,
                        0,
                        cnt.miniThumbnail?.size ?: 0
                    ).asImageBitmap()
                    content = MediaUiState.Thumbnail(
                        data = bitmap,
                        onDownloadClick = {}
                    )
                }
                if(photoSize != null) {
                    val downloadPhoto = {
                        downloadMessageMedia(
                            fileId = photoSize.file.id,
                            messageId = msg.id,
                            onFile = { file, msg ->
                                var newMsg = msg
                                if(msg.content is MessageContent.Photo) {
                                    val content = cnt.copy(
                                        sizes = cnt.sizes.map { size ->
                                            if(size.type == photoSize.type) size.copy(file = file) else size
                                        }
                                    )
                                    newMsg = newMsg.copy(content = content)
                                }
                                newMsg
                            }
                        )
                    }
                    width = photoSize.width
                    heigh = photoSize.height
                    content = when {
                        photoSize.file.path.isNotBlank() -> {
                            MediaUiState.Image(
                                File(photoSize.file.path)
                            )
                        }
                        content is MediaUiState.Thumbnail -> content.copy(onDownloadClick = downloadPhoto)
                        else -> MediaUiState.Placeholder(downloadPhoto)
                    }
                }
                MessageContentUiState.Photo(
                    content = content,
                    caption = cnt.caption.ifBlank { null },
                    width = width,
                    height = heigh
                )
            }
            else -> msg.content.toInfo {
                downloadMessageMedia(
                    fileId = it,
                    messageId = msg.id
                )
            }
        }
        val originSenderTitle = when(val cnt = msg.forwardInfo?.origin) {
            is MessageOrigin.HiddenUser -> cnt.name
            is MessageOrigin.Channel -> chatById[cnt.id]?.title
            is MessageOrigin.Chat -> chatById[cnt.id]?.title
            is MessageOrigin.User -> userById[cnt.id]?.firstName
            else -> null
        }

        val properties = propertiesById[msg.id]
        val canDeleteForSelf = properties?.canDeleteForSelf ?: false
        val canDeleteForAll = properties?.canDeleteForAll ?: false

        return MessageUiState(
            id = msg.id,
            content = msgContent,
            isOutgoing = msg.isOutgoing,
            date = msg.date.toTimeString(),
            sender = sender,
            sendingState = msg.sendingState,
            originSenderTitle = originSenderTitle,
            canDeleteForSelf = canDeleteForSelf,
            canDeleteForAll = canDeleteForAll
        )
    }
}