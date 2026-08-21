package org.gaziz.birgram.features.chat.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.gaziz.birgram.core.ui.model.ChatTypeInfo
import org.gaziz.birgram.core.ui.usecase.GetChatAvatar
import org.gaziz.birgram.core.ui.usecase.GetMessageSenderInfo
import org.gaziz.birgram.features.chat.domain.usecase.GetChatById
import org.gaziz.birgram.features.chat.domain.usecase.GetChatMessages
import org.gaziz.birgram.features.chat.domain.usecase.GetPhotoBySizes
import org.gaziz.birgram.features.chat.domain.usecase.LoadChatMessages
import org.gaziz.birgram.features.chat.ui.mapper.formatMonthDay
import org.gaziz.birgram.features.chat.ui.mapper.toInfo
import org.gaziz.birgram.features.chat.ui.mapper.toTimeString
import org.gaziz.birgram.features.chat.ui.model.ChatUiState
import org.gaziz.birgram.features.chat.ui.model.MediaContent
import org.gaziz.birgram.features.chat.ui.model.MessageContentInfo
import org.gaziz.birgram.features.chat.ui.model.MessageUiState
import org.gaziz.telegram.api.ChatService
import org.gaziz.telegram.api.GroupService
import org.gaziz.telegram.api.MessageService
import org.gaziz.telegram.api.UserService
import org.gaziz.telegram.api.model.chat.ChatType
import org.gaziz.telegram.api.model.group.GroupMemberStatus
import org.gaziz.telegram.api.model.message.DraftMessage
import org.gaziz.telegram.api.model.message.DraftMessageContent
import org.gaziz.telegram.api.model.message.MessageContent
import org.gaziz.telegram.api.model.message.MessageSender
import org.gaziz.telegram.api.model.user.UserType
import org.gaziz.telegram.api.usecase.DownloadMessageMedia
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatById: GetChatById,
    private val getChatMessages: GetChatMessages,
    private val chatService: ChatService,
    private val loadChatMessages: LoadChatMessages,
    private val userService: UserService,
    private val getChatAvatar: GetChatAvatar,
    private val groupService: GroupService,
    private val messageService: MessageService,
    private val getMessageSenderInfo: GetMessageSenderInfo,
    private val downloadMessageMedia: DownloadMessageMedia,
    private val getPhotoBySizes: GetPhotoBySizes
): ViewModel() {
    private var isLoading = false
    fun openChat(
        chatId: Long,
    ) {
        chatService.openChat(chatId) {
            isLoading = true
            loadChatMessages(chatId, onResp = { isLoading = false } )
        }
    }
    fun closeChat(
        chatId: Long,
    ) {
        chatService.closeChat(chatId)
    }
    val chat: (Long) -> StateFlow<ChatUiState?> = {
        getChatById(it).map { chat ->
            chat ?: return@map null
            val chatType = chat.type
            val isDeleted =
                chatType is ChatType.Private &&
                userService.users.value[chatType.userId]?.type == UserType.Deleted &&
                userService.users.value[chatType.userId]?.type == UserType.Unknown
            val avatar = getChatAvatar(chat)
            var canSendTextMessages = chat.permissions.canSendBasicMessages
            val typeInfo: ChatTypeInfo? = when(val type = chat.type) {
                is ChatType.BasicGroup -> {
                    val group = groupService.basicGroups.value[type.groupId]
                    canSendTextMessages =
                        chat.permissions.canSendBasicMessages ||
                        group?.memberStatus is GroupMemberStatus.Creator ||
                        (group?.memberStatus is GroupMemberStatus.Admin &&
                        (group.memberStatus as GroupMemberStatus.Admin).canPostMessages)
                    if(group != null) {
                        ChatTypeInfo.BasicGroup(
                            memberCount = group.memberCount,
                        )
                    } else {
                        null
                    }
                }
                is ChatType.SuperGroup -> {
                    val group = groupService.superGroups.value[type.groupId]
                    canSendTextMessages =
                        chat.permissions.canSendBasicMessages ||
                        group?.memberStatus is GroupMemberStatus.Creator ||
                        (group?.memberStatus is GroupMemberStatus.Admin &&
                        (group.memberStatus as GroupMemberStatus.Admin).canPostMessages)
                    if(group != null) {
                        ChatTypeInfo.SuperGroup(
                            memberCount = group.memberCount,
                            isChannel = type.isChannel
                        )
                    } else {
                        null
                    }
                }
                is ChatType.Private -> {
                    val user = userService.users.value[type.userId]
                    if(user != null) {
                        ChatTypeInfo.User(
                            status = user.status,
                            isBot = user.type is UserType.Bot
                        )
                    } else {
                        null
                    }
                }
                is ChatType.Secret -> {
                    val user = userService.users.value[type.userId]
                    if(user != null) {
                        ChatTypeInfo.User(
                            status = user.status,
                            isBot = user.type is UserType.Bot
                        )
                    } else {
                        null
                    }
                }
                else -> null
            }
            var draftMessageText = ""
            val draftMsg = chat.draftMessage
            if(
                draftMsg != null &&
                draftMsg.content is DraftMessageContent.Text
            ) {
                draftMessageText = (draftMsg.content as DraftMessageContent.Text).text
            }
            ChatUiState(
                id = chat.id,
                title = chat.title,
                avatar = avatar,
                isDeleted = isDeleted,
                typeInfo = typeInfo,
                draftText = draftMessageText,
                canSendTextMessages = canSendTextMessages,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
    }
    val messages: (Long) -> StateFlow<Map<String, List<MessageUiState>>> = { msgId ->
        getChatMessages(msgId).map { map ->
            map.entries.associate { (key,value) ->
                val messages = value.mapIndexed { ind, msg ->
                    val chat = chatService.chats.value[msg.chatId]
                    val senderInfo = getMessageSenderInfo(msg.sender)
                        .map { dt ->
                            var newData = dt?.copy(name = null, avatar = null)
                            val preMsg = value.getOrNull(ind+1)
                            if(preMsg != null) {
                                if(msg.sender != preMsg.sender) {
                                    newData = newData?.copy(name = dt?.name)
                                }
                            } else {
                                newData = newData?.copy(name = dt?.name)
                            }
                            val nextMsg = value.getOrNull(ind-1)
                            if(nextMsg != null) {
                                if(msg.sender != nextMsg.sender) {
                                    newData = newData?.copy(avatar = dt?.avatar)
                                }
                            } else {
                                newData = newData?.copy(avatar = dt?.avatar)
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
                        .stateIn(CoroutineScope(Dispatchers.IO))
                    val msgContent = when(val cnt = msg.content) {
                        is MessageContent.Photo -> {
                            val photoSize = getPhotoBySizes(cnt.sizes)
                            var width = 150
                            var heigh = 150
                            var content: MediaContent? = null
                            if(cnt.miniThumbnail != null) {
                                val bitmap = BitmapFactory.decodeByteArray(
                                    cnt.miniThumbnail,
                                    0,
                                    cnt.miniThumbnail?.size ?: 0
                                ).asImageBitmap()
                                content = MediaContent.Thumbnail(
                                    data = bitmap,
                                    downloadMedia = {}
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
                                        MediaContent.Image(
                                            File(photoSize.file.path)
                                        )
                                    }
                                    content is MediaContent.Thumbnail -> content.copy(downloadMedia = downloadPhoto)
                                    else -> MediaContent.PlaceHolder(downloadPhoto)
                                }
                            }
                            MessageContentInfo.Photo(
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
                    MessageUiState(
                        id = msg.id,
                        content = msgContent,
                        isOutgoing = msg.isOutgoing,
                        date = msg.date.toTimeString(),
                        sender = senderInfo.value
                    )
                }
                key.formatMonthDay() to messages
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyMap()
        )
    }
    fun loadMessages(
        chatId: Long,
        fromMessageId: Long
    ){
        if(isLoading) return
        isLoading = true
        loadChatMessages(
            chatId,
            fromMessageId
        ) {
            isLoading = false
        }
    }
    fun setDraftMessageText(
        chatId: Long,
        draft: String
    ) {
        messageService.setDraftMessage(
            chatId,
            DraftMessage(
                content = DraftMessageContent.Text(draft),
                date = LocalDateTime.now()
            )
        )
    }
    fun sendMessageText(
        chatId: Long,
        message: String
    ) {
        messageService.sendMessage(chatId,message)
    }

    private val _mediaId = MutableStateFlow<Long?>(null)
    val mediaId = _mediaId.asStateFlow()
    fun setMediaId(msgId: Long){
        _mediaId.update { msgId }
    }
    var player: ExoPlayer? = null
    fun createPlayer(
        context: Context
    ) {
        player = ExoPlayer
            .Builder(context)
            .build()
            .apply {
                playWhenReady = true
                addListener(
                    object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            if (!isPlaying && mediaItemCount > 0) {
                                seekTo(0L)
                                play()
                            }
                        }
                    }
                )
            }
    }
    fun setPlayerMedia(uri: Uri) {
        player?.apply {
            val media = MediaItem.fromUri(uri)
            setMediaItem(media,true)
            prepare()
        }
    }
    fun removePlayerMedia(){
        player?.apply {
            stop()
            clearMediaItems()
            _mediaId.update { null }
        }
    }
    fun releasePlayer() {
        player?.release()
        _mediaId.update { null }
        player = null
    }
}