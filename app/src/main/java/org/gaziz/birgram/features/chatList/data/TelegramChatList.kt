package org.gaziz.birgram.features.chatList.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.core.telegram.mapper.toFileData
import org.gaziz.birgram.core.telegram.mapper.toPhotoInfo
import org.gaziz.birgram.core.telegram.mapper.toStatus
import org.gaziz.birgram.core.telegram.model.ChatPhoto
import org.gaziz.birgram.core.telegram.model.ChatType
import org.gaziz.birgram.core.telegram.model.RequestResponse
import org.gaziz.birgram.features.chatList.data.mapper.toChatData
import org.gaziz.birgram.features.chatList.data.mapper.toChatPosition
import org.gaziz.birgram.features.chatList.data.mapper.toLastMsgData
import org.gaziz.birgram.features.chatList.domain.model.ChatData
import org.gaziz.birgram.features.chatList.domain.model.ChatListType
import org.gaziz.birgram.features.chatList.domain.model.ChatPosition
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import javax.inject.Inject

class TelegramChatList @Inject constructor(
    private val manager: TelegramManager
): ChatListRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            collectUpdates()
        }
    }

    private val _chats = MutableStateFlow(emptyMap<Long, ChatData>())
    override val chats: StateFlow<Map<Long, ChatData>> = _chats.asStateFlow()

    private suspend fun collectUpdates() {
        manager.update.collect { u ->
            when(u) {
                is TdApi.UpdateNewChat -> {
                    val chat = u.chat.toChatData()
                    _chats.update { map ->
                        map.toMutableMap().apply { put(chat.id, chat) }.toMap()
                    }
                }

                is TdApi.UpdateChatPosition -> {
                    _chats.update { map ->
                        val newMap = map.toMutableMap()
                        val chat = newMap[u.chatId]
                        if(chat != null) {
                            val position = u.position.toChatPosition()
                            if(position.order == 0L) {
                                newMap.remove(u.chatId)
                            } else {
                                newMap[u.chatId] = chat.copy(
                                    positions = chat.positions.filter {
                                        it.listType != position.listType
                                    } + position
                                )
                            }
                        }
                        newMap.toMap()
                    }
                }

                is TdApi.UpdateChatLastMessage -> {
                    _chats.update { map ->
                        val newMap = map.toMutableMap()
                        val chat = newMap[u.chatId]
                        if(chat != null){
                            val positions = mutableListOf<ChatPosition>()
                                .apply { u.positions.forEach { add(it.toChatPosition()) } }
                                .toList()
                            val lastMsg = u.lastMessage.toLastMsgData()
                            newMap[u.chatId] = chat.copy(
                                positions = positions,
                                lastMessage = lastMsg ?: chat.lastMessage
                            )
                        }
                        newMap.toMap()
                    }
                }

                is TdApi.UpdateChatPhoto -> {
                    _chats.update {
                        val newMap = it.toMutableMap()
                        val chat = newMap[u.chatId]
                        if(chat != null) {
                            newMap[u.chatId] = chat.copy(photo = u.photo.toPhotoInfo())
                        }
                        newMap.toMap()
                    }
                }

                is TdApi.UpdateFile -> {
                    if(u.file.local.isDownloadingCompleted) {
                        _chats.update { map ->
                            val newMap = map.toMutableMap()
                            for(chat in newMap.map { it.value }){
                                if(chat.photo != null) {
                                    if(chat.photo.small.id == u.file.id) {
                                        newMap[chat.id] = chat.copy(
                                            photo = ChatPhoto(
                                                chat.photo.miniThumbnail,
                                                u.file.toFileData()
                                            )
                                        )
                                        break
                                    }
                                }
                            }
                            newMap.toMap()
                        }
                    }
                }

                is TdApi.UpdateChatReadInbox -> {
                    _chats.update {
                        val newMap = it.toMutableMap()
                        newMap[u.chatId]?.let { chat ->
                            newMap[u.chatId] = chat.copy(unreadCount = u.unreadCount)
                        }
                        newMap.toMap()
                    }
                }

                is TdApi.UpdateChatUnreadMentionCount -> {
                    _chats.update {
                        val newMap = it.toMutableMap()
                        newMap[u.chatId]?.let { chat ->
                            newMap[u.chatId] = chat.copy(unreadCount = u.unreadMentionCount)
                        }
                        newMap.toMap()
                    }
                }

                is TdApi.UpdateChatUnreadReactionCount -> {
                    _chats.update {
                        val newMap = it.toMutableMap()
                        newMap[u.chatId]?.let { chat ->
                            newMap[u.chatId] = chat.copy(unreadCount = u.unreadReactionCount)
                        }
                        newMap.toMap()
                    }
                }

                is TdApi.UpdateUserStatus -> {
                    manager.sendRequest(
                        TdApi.CreatePrivateChat().apply {
                            this.userId = u.userId
                            this.force = false
                        },
                        {}
                    ) {
                        if(it is TdApi.Chat){
                            val respChat = it.toChatData()
                            _chats.update {
                                val newMap = it.toMutableMap()
                                val chat = newMap[respChat.id]
                                if(chat != null) {
                                    newMap[respChat.id] = chat.copy(type =
                                        ChatType.Private(
                                            userId = u.userId,
                                            userStatus = u.status.toStatus()
                                        )
                                    )
                                }
                                newMap.toMap()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (RequestResponse.Error) -> Unit
    ) {
        manager.sendRequest(
            TdApi.LoadChats().apply {
                this.limit = limit
                this.chatList  = when(listType) {
                    is ChatListType.Folder -> TdApi.ChatListFolder(listType.id)
                    ChatListType.Archive ->  TdApi.ChatListArchive()
                    ChatListType.Main -> TdApi.ChatListMain()
                }
            },
            {
                onError(it)
            }
        )
    }

    override fun downloadChatPhoto(fileId: Int) {
        manager.sendRequest(
            TdApi.DownloadFile().apply {
                this.fileId = fileId
                this.priority = 32
                this.limit = 0
                this.offset = 0
                this.synchronous = false
            },
            {}
        )
    }

    override fun logOut(onOk: () -> Unit) {
        manager.sendRequest(
            TdApi.LogOut(),
            {},
        ) {
            if(it is TdApi.Ok) {
                _chats.value = emptyMap()
                onOk()
            }
        }
    }
}