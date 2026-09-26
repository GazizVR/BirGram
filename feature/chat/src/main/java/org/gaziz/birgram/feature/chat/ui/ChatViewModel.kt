package org.gaziz.birgram.feature.chat.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.group.GroupMemberStatus
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import org.gaziz.birgram.core.telegram.api.model.message.MessageOrigin
import org.gaziz.birgram.core.telegram.api.model.message.MessageSender
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.api.usecase.DownloadMessageMedia
import org.gaziz.birgram.core.ui.model.ChatTypeInfo
import org.gaziz.birgram.core.ui.usecase.GetChatAvatar
import org.gaziz.birgram.core.ui.usecase.GetMessageSenderInfo
import org.gaziz.birgram.feature.chat.domain.usecase.GetChatById
import org.gaziz.birgram.feature.chat.domain.usecase.GetChatMessages
import org.gaziz.birgram.feature.chat.domain.usecase.GetPhotoBySizes
import org.gaziz.birgram.feature.chat.domain.usecase.LoadChatMessages
import org.gaziz.birgram.feature.chat.ui.mapper.formatMonthDay
import org.gaziz.birgram.feature.chat.ui.mapper.toInfo
import org.gaziz.birgram.feature.chat.ui.mapper.toTimeString
import org.gaziz.birgram.feature.chat.ui.model.ChatUiState
import org.gaziz.birgram.feature.chat.ui.model.MediaContent
import org.gaziz.birgram.feature.chat.ui.model.MessageContentInfo
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState
import java.io.File
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,

    getChatById: GetChatById,
    getChatMessages: GetChatMessages,
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
    private val chatId = checkNotNull<Long>(savedStateHandle["chatId"])
    private var historyLoading = false
    init {
        createPlayer()
        chatService.openChat(chatId) {
            historyLoading = true
            loadChatMessages(
                chatId,
                onResp = { historyLoading = false }
            )
        }
    }
    override fun onCleared() {
        chatService.closeChat(chatId)
        releasePlayer()
    }
    val chat: StateFlow<ChatUiState?> =
        getChatById(chatId).map { chat ->
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
                unreadCount = if(chat.unreadCount > 0) chat.unreadCount else null,
                lastMessage = chat.lastMessage
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    val messages: StateFlow<Map<String, List<MessageUiState>>> =
        combine(
            getChatMessages(chatId),
            messageService.messageProperties
        ) { dateToMessagesMap, propertiesMap ->
            dateToMessagesMap.entries.associate { (key,value) ->
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
                    val originSenderTitle = when(val cnt = msg.forwardInfo?.origin) {
                        is MessageOrigin.HiddenUser -> cnt.name
                        is MessageOrigin.Channel -> getChatById(cnt.id).stateIn(viewModelScope).value?.title
                        is MessageOrigin.Chat -> getChatById(cnt.id).stateIn(viewModelScope).value?.title
                        is MessageOrigin.User -> {
                            userService.users
                                .mapNotNull { it[cnt.id] }
                                .stateIn(viewModelScope)
                                .value.firstName
                        }
                        else -> null
                    }

                    val properties = propertiesMap[msg.id]
                    val canDeleteForSelf = properties?.canDeleteForSelf ?: false
                    val canDeleteForAll = properties?.canDeleteForAll ?: false

                    MessageUiState(
                        id = msg.id,
                        content = msgContent,
                        isOutgoing = msg.isOutgoing,
                        date = msg.date.toTimeString(),
                        sender = senderInfo.value,
                        sendingState = msg.sendingState,
                        originSenderTitle = originSenderTitle,
                        canDeleteForSelf = canDeleteForSelf,
                        canDeleteForAll = canDeleteForAll
                    )
                }
                key.formatMonthDay() to messages
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyMap()
        )

    fun loadMessages(fromMessageId: Long){
        if(historyLoading) return
        historyLoading = true
        loadChatMessages(
            chatId,
            fromMessageId
        ) {
            historyLoading = false
        }
    }
    fun setDraftMessageText(draft: String) {
        messageService.setDraftMessage(
            chatId,
            DraftMessage(
                content = DraftMessageContent.Text(draft),
                date = LocalDateTime.now()
            )
        )
    }
    fun sendMessageText(message: String) {
        messageService.sendMessage(chatId,message)
    }
    fun deleteMessages(
        msgIds: LongArray,
        forAll: Boolean
    ) {
        messageService.deleteMessages(
            chatId = chatId,
            msgIds = msgIds,
            forAll = forAll
        )
    }
    fun loadMessageProperties(messageId: Long) {
       messageService.loadMessageProperties(chatId,messageId)
    }

    private val _mediaId = MutableStateFlow<Long?>(null)
    val mediaId = _mediaId.asStateFlow()
    fun setMediaId(msgId: Long){
        _mediaId.update { msgId }
    }
    var player: ExoPlayer? = null
    private val _mediaPosition = MutableStateFlow(0)
    val mediaPosition = _mediaPosition.asStateFlow()
    private var _isMediaPlaying = MutableStateFlow(false)
    val isMediaPlaying = _isMediaPlaying.asStateFlow()
    private fun createPlayer() {
        player = ExoPlayer
            .Builder(context)
            .build()
            .apply {
                playWhenReady = true
                addListener(
                    object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            _isMediaPlaying.update { isPlaying }
                            if(mediaItemCount > 0) {
                                if (!isPlaying) {
                                    seekTo(0L)
                                    play()
                                } else {
                                    viewModelScope.launch {
                                        _mediaPosition.update { (duration/1000).toInt() }
                                        while(
                                            mediaPosition.value > 0 &&
                                            isMediaPlaying.value
                                        ){
                                            delay(1000)
                                            _mediaPosition.update { it-1 }
                                        }
                                    }
                                }
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