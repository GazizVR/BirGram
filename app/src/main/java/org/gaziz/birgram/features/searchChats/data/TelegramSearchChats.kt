package org.gaziz.birgram.features.searchChats.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.updaters.TelegramChat
import org.gaziz.birgram.features.searchChats.data.mapper.toChatData
import org.gaziz.birgram.features.searchChats.domain.model.ChatData
import org.gaziz.birgram.features.searchChats.domain.repository.SearchChatsRepository
import javax.inject.Inject

class TelegramSearchChats @Inject constructor(
    private val manager: ClientManager,
    private val tgChat: TelegramChat
): SearchChatsRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            tgChat.chats.collect { chats ->
                _searchedChats.update { map ->
                    map.toMutableMap().apply {
                        keys.forEach { key ->
                            chats[key]?.let{ chat ->
                                put(key,chat.toChatData())
                            }
                        }
                    }.toMap()
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
        _searchedChats.update { emptyMap() }
        manager.sendRequest(
            TdApi.SearchChats().apply {
                this.query = query
                this.limit = limit
            },
            {},
            {
                if(it is TdApi.Chats) {
                    _searchedChats.update { map ->
                        map.toMutableMap().apply {
                            it.chatIds.forEach { chatId ->
                                tgChat.chats.value[chatId]?.let { chat ->
                                    put(chatId,chat.toChatData())
                                }
                            }
                        }.toMap()
                    }
                }
            }
        )
    }

    override fun downloadChatIcon(
        chatId: Long,
        fileId: Int
    ) {
        tgChat.downloadChatPhotoSmall(chatId,fileId)
    }

}