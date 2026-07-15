package org.gaziz.birgram.features.searchChats.data

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
import org.gaziz.birgram.features.searchChats.data.mapper.toChatData
import org.gaziz.birgram.features.searchChats.domain.model.ChatData
import org.gaziz.birgram.features.searchChats.domain.repository.SearchChatsRepository
import javax.inject.Inject

class TelegramSearchChats @Inject constructor(
    private val manager: TelegramManager
): SearchChatsRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            collectUpdates()
        }
    }

    private val _chats = MutableStateFlow<Map<Long, ChatData>>(emptyMap())

    private suspend fun collectUpdates() {
        manager.update.collect { u ->
            when(u) {
                is TdApi.UpdateNewChat -> {
                    val chat = u.chat.toChatData()
                    _chats.update { map ->
                        map.toMutableMap().apply { put(chat.id, chat) }.toMap()
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

                is TdApi.UpdateBasicGroup -> {
                    manager.sendRequest(
                        TdApi.CreateBasicGroupChat().apply {
                            this.basicGroupId = u.basicGroup.id
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
                                    newMap[respChat.id] = chat.copy(
                                        type = ChatType.BasicGroup(u.basicGroup.id,u.basicGroup.memberCount)
                                    )
                                }
                                newMap.toMap()
                            }
                        }
                    }
                }

                is TdApi.UpdateSupergroup -> {
                    manager.sendRequest(
                        TdApi.CreateSupergroupChat().apply {
                            this.supergroupId = u.supergroup.id
                            this.force = false
                        },
                        {}
                    ) {
                        if(it is TdApi.Chat){
                            val respChat = it.toChatData()
                            _chats.update {
                                val newMap = it.toMutableMap()
                                val chat = newMap[respChat.id]
                                val canSend = when (u.supergroup.status) {
                                    is TdApi.ChatMemberStatusCreator -> true
                                    is TdApi.ChatMemberStatusAdministrator -> {
                                        (u.supergroup.status as TdApi.ChatMemberStatusAdministrator).rights.canPostMessages
                                    }
                                    else -> false
                                }
                                if(chat != null) {
                                    newMap[respChat.id] = chat.copy(
                                        type = ChatType.SuperGroup(
                                            u.supergroup.id,
                                            u.supergroup.isChannel,
                                            u.supergroup.memberCount,
                                            canSend
                                        )
                                    )
                                }
                                newMap.toMap()
                            }
                        }
                    }
                }

                is TdApi.UpdateUserStatus -> {
                    manager.sendRequest(
                        TdApi.CreatePrivateChat().apply {
                            this.userId = u.userId
                            this.force = false
                        },
                        {}
                    ) { obj ->
                        if(obj is TdApi.Chat){
                            val respChat = obj.toChatData()
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
    private val _searchedChats = MutableStateFlow<Map<Long, ChatData>>(emptyMap())
    override val searchedChats: StateFlow<Map<Long, ChatData>> = _searchedChats.asStateFlow()

    override fun searchLocal(
        query: String,
        limit: Int
    ) {
        manager.sendRequest(
            TdApi.SearchChats().apply {
                this.query = query
                this.limit = limit
            },
            {},
            {
                if(it is TdApi.Chats) {

                }
            }
        )
    }

    override fun downloadPhoto(fileId: Int) {
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

}