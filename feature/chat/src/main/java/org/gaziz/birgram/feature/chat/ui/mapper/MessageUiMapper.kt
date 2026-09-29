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
import org.gaziz.birgram.core.telegram.ui.mapper.MessageSenderUiMapper
import org.gaziz.birgram.feature.chat.domain.usecase.DownloadMessageMedia
import org.gaziz.birgram.feature.chat.domain.usecase.GetPhotoBySizes
import org.gaziz.birgram.feature.chat.ui.model.MediaUiState
import org.gaziz.birgram.feature.chat.ui.model.MessageContentUiState
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class MessageUiMapper @Inject constructor(
    private val getPhotoBySizes: GetPhotoBySizes,
    private val downloadMessageMedia: DownloadMessageMedia,
    private val messageSenderUiMapper: MessageSenderUiMapper,
    private val messageContentUiMapper: MessageContentUiMapper
) {
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private fun LocalDateTime.toTimeString(): String = format(timeFormatter)

    fun map(
        msg: Message,
        prevMsg: Message? = null,
        nextMsg: Message? = null,
        chatsById: Map<Long, Chat>,
        usersById: Map<Long, User>,
        propertiesById: Map<Long, MessageProperties>
    ): MessageUiState {
        val chat = chatsById[msg.chatId]
        val sender = run {
            val sender = messageSenderUiMapper(msg.sender,chatsById,usersById)
            var newSender = sender?.copy(name = null, avatar = null)
            if(prevMsg != null) {
                if(msg.sender != prevMsg.sender) {
                    newSender = newSender?.copy(name = sender?.name)
                }
            } else {
                newSender = newSender?.copy(name = sender?.name)
            }
            if(nextMsg != null) {
                if(msg.sender != nextMsg.sender) {
                    newSender = newSender?.copy(avatar = sender?.avatar)
                }
            } else {
                newSender = newSender?.copy(avatar = sender?.avatar)
            }
            if(chat?.type is ChatType.Private || chat?.type is ChatType.Secret) {
                newSender = null
            }
            if(
                msg.sender is MessageSender.Chat &&
                (msg.sender as MessageSender.Chat).id == msg.chatId
            ) {
                newSender = null
            }
            newSender
        }
        val msgContent = when(val cnt = msg.content) {
            is MessageContent.Photo -> {
                val photoSize = getPhotoBySizes(cnt.sizes)
                var width = 150
                var height = 150
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
                    height = photoSize.height
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
                    height = height
                )
            }
            else -> messageContentUiMapper.map(
                msg.content
            ) {
                downloadMessageMedia(
                    fileId = it,
                    messageId = msg.id
                )
            }
        }
        val originSenderTitle = when(val cnt = msg.forwardInfo?.origin) {
            is MessageOrigin.HiddenUser -> cnt.name
            is MessageOrigin.Channel -> chatsById[cnt.id]?.title
            is MessageOrigin.Chat -> chatsById[cnt.id]?.title
            is MessageOrigin.User -> usersById[cnt.id]?.firstName
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