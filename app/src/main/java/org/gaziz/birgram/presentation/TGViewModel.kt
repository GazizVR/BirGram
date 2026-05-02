package org.gaziz.birgram.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.data.remote.Auth
import org.gaziz.birgram.data.local.UserPreferences


sealed class DownloadType {
    data class ChatPhoto(val chatKey: Long): DownloadType()
}

sealed class Direction {
    object Right: Direction()
    object Left: Direction()
    object Up: Direction()
    object Down: Direction()
}

class TGViewModel(
    val userPreferences: UserPreferences,
    val tdLib: Auth
): ViewModel() {
    var isPendingRecovery by mutableStateOf<TdApi.EmailAddressResetStatePending?>(null)
    var isAvailableRecovery by mutableStateOf<TdApi.EmailAddressResetStateAvailable?>(null)
    var isAccountDelete by mutableStateOf(false)
    var isTerms by mutableStateOf(false)
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")

    private val downloadType = MutableStateFlow<DownloadType>(DownloadType.ChatPhoto(0))

    private val _chats = MutableStateFlow<Map<Long, TdApi.Chat>>(emptyMap())
    val chats = _chats.asStateFlow()

    val folderChats = mutableStateMapOf<Int,Map<Long,TdApi.Chat>>()
    val archiveChats = mutableStateMapOf<Long,TdApi.Chat>()

    private val _folders = MutableStateFlow<List<TdApi.ChatFolderInfo>>(emptyList())
    val folders = _folders.asStateFlow()

    private val _unreadChatCounts = MutableStateFlow<Map<TdApi.ChatList,Int>>(emptyMap())
    val unreadChatCounts = _unreadChatCounts.asStateFlow()

    var targetChatList by mutableStateOf<TdApi.ChatList>(TdApi.ChatListMain())
    var animationDirection by mutableStateOf<Direction>(Direction.Right)

    private val _chatsPhotos = MutableStateFlow<Map<Long,Any?>>(emptyMap())
    val chatsPhotos = _chatsPhotos.asStateFlow()

    var isNewAccount by mutableStateOf(false)

    val isDarkTheme = userPreferences.isDark.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        true
    )

    fun switchIsDark(){
        viewModelScope.launch {
            userPreferences.switchIsDark(!isDarkTheme.value)
        }
    }

    init {
        tdLib.initClient(
            {
                _apiState.value = ApiState.Error(it)
            },
            {
                viewModelScope.launch {
                    _loginState.value = it
                    _apiState.value = ApiState.Init
                }
            },
            { position,chat,lastMessage,outbox,photo,title,mention,reaction,unread ->
                when {
                    chat != null -> {
                        isNewAccount = false
                        val chatPhoto =
                            when {
                                chat.photo?.small?.local?.path != null && chat.photo?.small?.local?.isDownloadingCompleted == true -> {
                                    chat.photo?.small?.local?.path
                                }
                                chat.photo?.minithumbnail != null -> chat.photo?.minithumbnail?.data
                                else -> null
                            }
                        _chatsPhotos.update { map ->
                            map + (chat.id to chatPhoto)
                        }

                        _chats.update { map ->
                            map + (chat.id to chat)
                        }
                    }
                    lastMessage != null -> {
                        isNewAccount = false
                        if(chats.value.containsKey(lastMessage.chatId)){
                            val preChat = chats.value[lastMessage.chatId]!!
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = lastMessage.lastMessage
                                this.positions = lastMessage.positions
                                this.photo = preChat.photo
                                this.title = preChat.title
                                this.unreadCount = preChat.unreadCount
                                this.unreadMentionCount = preChat.unreadMentionCount
                                this.unreadReactionCount = preChat.unreadReactionCount
                                this.lastReadOutboxMessageId = preChat.lastReadOutboxMessageId
                            }
                            _chats.update { map ->
                                map + (lastMessage.chatId to chat)
                            }
                        }
                    }
                    position != null -> {
                        isNewAccount = false
                        if(chats.value.containsKey(position.chatId)){
                            val preChat = chats.value[position.chatId]!!
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = preChat.lastMessage
                                this.photo = preChat.photo
                                this.title = preChat.title
                                this.unreadCount = preChat.unreadCount
                                this.unreadMentionCount = preChat.unreadMentionCount
                                this.unreadReactionCount = preChat.unreadReactionCount
                                this.lastReadOutboxMessageId = preChat.lastReadOutboxMessageId
                                this.positions = preChat.positions
                                    .toMutableList()
                                    .apply {
                                        preChat.positions
                                            .find { position.position.list == it.list }
                                            ?.let { remove(it) }
                                        add(position.position)
                                    }
                                    .toTypedArray()
                            }
                            _chats.update { map ->
                                map + (position.chatId to chat)
                            }
                            if(
                                chat.photo?.small?.local?.canBeDownloaded == true &&
                                chat.photo?.small?.local?.isDownloadingCompleted == false &&
                                chat.photo?.small?.local?.isDownloadingActive == false
                            ){
                                downloadChatPhoto(
                                    chat.photo?.small?.id ?: 0,
                                    chat.photo?.small?.local?.downloadOffset ?: 0,
                                    position.chatId
                                )
                            }
                        }
                    }

                    photo != null -> {
                        isNewAccount = false
                        if(chats.value.containsKey(photo.chatId)){
                            val preChat = chats.value[photo.chatId]!!
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = preChat.lastMessage
                                this.photo = photo.photo
                                this.title = preChat.title
                                this.unreadCount = preChat.unreadCount
                                this.unreadMentionCount = preChat.unreadMentionCount
                                this.unreadReactionCount = preChat.unreadReactionCount
                                this.positions = preChat.positions
                                this.lastReadOutboxMessageId = preChat.lastReadOutboxMessageId
                            }
                            val chatPhoto =
                                when {
                                    chat.photo?.small?.local?.path != null && chat.photo?.small?.local?.isDownloadingCompleted == true -> {
                                        chat.photo?.small?.local?.path
                                    }
                                    chat.photo?.minithumbnail != null -> chat.photo?.minithumbnail?.data
                                    else -> null
                                }
                            _chatsPhotos.update { map ->
                                map + (chat.id to chatPhoto)
                            }
                            _chats.update { map ->
                                map + (photo.chatId to chat)
                            }
                            if(photo.photo?.small?.local?.canBeDownloaded == true){
                                downloadChatPhoto(
                                    photo.photo?.small?.id ?: 0,
                                    photo.photo?.small?.local?.downloadOffset ?: 0,
                                    photo.chatId
                                )
                            }
                        }
                    }

                    title != null -> {
                        isNewAccount = false
                        if(chats.value.containsKey(title.chatId)){
                            val preChat = chats.value[title.chatId]!!
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = preChat.lastMessage
                                this.photo = preChat.photo
                                this.title = title.title
                                this.unreadCount = preChat.unreadCount
                                this.unreadMentionCount = preChat.unreadMentionCount
                                this.unreadReactionCount = preChat.unreadReactionCount
                                this.positions = preChat.positions
                                this.lastReadOutboxMessageId = preChat.lastReadOutboxMessageId
                            }
                            _chats.update { map ->
                                map + (title.chatId to chat)
                            }
                        }
                    }

                    outbox != null -> {
                        isNewAccount = false
                        if(chats.value.containsKey(outbox.chatId)){
                            val preChat = chats.value[outbox.chatId]!!
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = preChat.lastMessage
                                this.photo = preChat.photo
                                this.title = preChat.title
                                this.unreadCount = preChat.unreadCount
                                this.unreadMentionCount = preChat.unreadMentionCount
                                this.unreadReactionCount = preChat.unreadReactionCount
                                this.positions = preChat.positions
                                this.lastReadOutboxMessageId = outbox.lastReadOutboxMessageId
                            }
                            _chats.update { map ->
                                map + (outbox.chatId to chat)
                            }
                        }
                    }

                    unread != null -> {
                        isNewAccount = false
                        if(chats.value.containsKey(unread.chatId)){
                            val preChat = chats.value[unread.chatId]!!
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = preChat.lastMessage
                                this.photo = preChat.photo
                                this.title = preChat.title
                                this.unreadCount = unread.unreadCount
                                this.unreadMentionCount = preChat.unreadMentionCount
                                this.unreadReactionCount = preChat.unreadReactionCount
                                this.positions = preChat.positions
                                this.lastReadOutboxMessageId = preChat.lastReadOutboxMessageId
                            }
                            _chats.update { map ->
                                map + (unread.chatId to chat)
                            }
                        }
                    }

                    mention != null -> {
                        isNewAccount = false
                        if(chats.value.containsKey(mention.chatId)){
                            val preChat = chats.value[mention.chatId]!!
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = preChat.lastMessage
                                this.photo = preChat.photo
                                this.title = preChat.title
                                this.unreadCount = preChat.unreadCount
                                this.unreadMentionCount = mention.unreadMentionCount
                                this.unreadReactionCount = preChat.unreadReactionCount
                                this.positions = preChat.positions
                                this.lastReadOutboxMessageId = preChat.lastReadOutboxMessageId
                            }
                            _chats.update { map ->
                                map + (mention.chatId to chat)
                            }
                        }
                    }

                    reaction != null -> {
                        isNewAccount = false
                        if(chats.value.containsKey(reaction.chatId)){
                            val preChat = chats.value[reaction.chatId]!!
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = preChat.lastMessage
                                this.photo = preChat.photo
                                this.title = preChat.title
                                this.unreadCount = preChat.unreadCount
                                this.unreadMentionCount = preChat.unreadMentionCount
                                this.unreadReactionCount = reaction.unreadReactionCount
                                this.positions = preChat.positions
                                this.lastReadOutboxMessageId = preChat.lastReadOutboxMessageId
                            }
                            _chats.update { map ->
                                map + (reaction.chatId to chat)
                            }
                        }
                    }

                    else -> {
                        isNewAccount = true
                    }
                }
            },
            { file ->
                when(val type = downloadType.value){
                    is DownloadType.ChatPhoto -> {
                        if(chats.value.containsKey(type.chatKey)){
                            val preChat = chats.value[type.chatKey]!!
                            val photo = TdApi.ChatPhotoInfo().apply {
                                this.minithumbnail = preChat.photo?.minithumbnail
                                this.small = file
                            }
                            val chat = TdApi.Chat().apply {
                                this.lastMessage = preChat.lastMessage
                                this.positions = preChat.positions
                                this.photo = photo
                                this.title = preChat.title
                                this.unreadCount = preChat.unreadCount
                                this.unreadMentionCount = preChat.unreadMentionCount
                                this.unreadReactionCount = preChat.unreadReactionCount
                            }
                            val chatPhoto =
                                when {
                                    chat.photo?.small?.local?.path != null && chat.photo?.small?.local?.isDownloadingCompleted == true -> {
                                        chat.photo?.small?.local?.path
                                    }
                                    chat.photo?.minithumbnail != null -> chat.photo?.minithumbnail?.data
                                    else -> null
                                }
                            _chatsPhotos.update { map ->
                                map + (chat.id to chatPhoto)
                            }
                            _chats.update {
                                it + (type.chatKey to chat)
                            }
                        }
                    }
                }
            },
            { folders ->
                val list =
                    listOf(
                        TdApi.ChatFolderInfo().apply {
                            name = TdApi.ChatFolderName().apply {
                                this.text = TdApi.FormattedText().apply {
                                    this.text = "All"
                                }
                            }
                            icon = TdApi.ChatFolderIcon().apply {
                                this.name = "All"
                            }
                        }
                    )
                _folders.update {
                    emptyList()
                }
                _folders.update {
                    list + folders.chatFolders.toList()
                }
            },
            { unread ->
                _unreadChatCounts.update {
                    it + (unread.chatList to unread.unreadCount)
                }
            }
        )
    }

    fun downloadChatPhoto(
        fileId: Int,
        offset: Long,
        chatKey: Long
    ){
        downloadType.value = DownloadType.ChatPhoto(chatKey)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                tdLib.downloadFile(fileId, 32,offset,0,true){_apiState.value = ApiState.Error(it)}
            } catch(it: Exception){
                val message = it.message ?: "unknown client exception"
                _apiState.value = ApiState.Error(message)
                Log.e("TDLib",message)
            }
        }
    }

    fun loadChats(
        type: TdApi.ChatList = TdApi.ChatListMain(),
        limit: Int = 100
    ){
        viewModelScope.launch {
            try {
                tdLib.loadChats(type,limit){_apiState.value = ApiState.Error(it)}
            } catch(it: Exception){
                val message = it.message ?: "unknown client exception"
                _apiState.value = ApiState.Error(message)
                Log.e("TDLib",message)
            }
        }
    }

}

class TGViewModelFactory(
    val userPreferences: UserPreferences,
    val tdLib: Auth
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TGViewModel(userPreferences,tdLib) as T
    }
}