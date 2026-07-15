package org.gaziz.birgram.features.searchChats.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.features.chatList.domain.model.ChatData
import org.gaziz.birgram.features.searchChats.domain.repository.SearchChatsRepository
import javax.inject.Inject

class TelegramSearchChats @Inject constructor(
    private val manager: TelegramManager
): SearchChatsRepository {
    private val _searchedChats = MutableStateFlow<Map<Long, ChatData>>(emptyMap())
    override val searchedChats: StateFlow<Map<Long, ChatData>> = _searchedChats.asStateFlow()
    override fun updateSearchChats(chat: ChatData?) {
        _searchedChats.update { map ->
            val newMap = mutableMapOf<Long, ChatData>()
            if(chat != null) {
                newMap.putAll(map)
                newMap[chat.id] = chat
            }
            newMap.toMap()
        }
    }
    override fun searchLocal(
        query: String,
        limit: Int,
        onResult: (List<Long>) -> Unit
    ) {
        manager.sendRequest(
            TdApi.SearchChats().apply {
                this.query = query
                this.limit = limit
            },
            {},
            {
                if(it is TdApi.Chats) {
                    onResult(it.chatIds.toList())
                } else {
                    onResult(emptyList())
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