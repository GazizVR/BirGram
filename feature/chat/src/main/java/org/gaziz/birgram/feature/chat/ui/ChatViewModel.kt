package org.gaziz.birgram.feature.chat.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.feature.chat.domain.usecase.GetChatById
import org.gaziz.birgram.feature.chat.domain.usecase.GetChatMessagesByDate
import org.gaziz.birgram.feature.chat.domain.usecase.LoadChatMessages
import org.gaziz.birgram.feature.chat.ui.mapper.ChatUiMapper
import org.gaziz.birgram.feature.chat.ui.mapper.MessageGroupMapper
import org.gaziz.birgram.feature.chat.ui.mapper.MessageUiMapper
import org.gaziz.birgram.feature.chat.ui.model.ChatUiState
import org.gaziz.birgram.feature.chat.ui.model.MessageUiState
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,

    getChatById: GetChatById,
    getChatMessagesByDate: GetChatMessagesByDate,
    private val loadChatMessages: LoadChatMessages,

    userService: UserService,
    groupService: GroupService,
    private val chatService: ChatService,
    private val messageService: MessageService,

    private val chatUiMapper: ChatUiMapper,
    private val messageGroupMapper: MessageGroupMapper,
    private val messageUiMapper: MessageUiMapper
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
        combine(
            getChatById(chatId),
            userService.users,
            groupService.basicGroups,
            groupService.superGroups
        ) { chat, usersById, basicGroupsById, superGroupsById ->
            chat ?: return@combine null
            chatUiMapper(chat,usersById,basicGroupsById,superGroupsById)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    val messages: StateFlow<Map<String, List<MessageUiState>>> =
        combine(
            getChatMessagesByDate(chatId),
            chatService.chats,
            userService.users,
            messageService.messageProperties
        ) { messagesByDate, chatsById, usersById, propertiesById ->
            messageGroupMapper.map(messagesByDate,chatsById,usersById,propertiesById)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyMap()
        )

    fun getMessageById(id: Long): StateFlow<MessageUiState?> {
        return combine(
            messageService.messages.map { it[id] },
            messageService.messageProperties.map { it[id] }
        ) { msg, properties ->
            msg ?: return@combine null
            messageUiMapper.map(msg, properties)
        }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                null
            )
    }

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
    fun resendMessages(msgIds: LongArray) {
        messageService.resendMessages(
            chatId = chatId,
            msgIds = msgIds
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