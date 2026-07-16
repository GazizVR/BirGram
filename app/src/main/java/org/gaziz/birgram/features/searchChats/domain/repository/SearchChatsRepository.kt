package org.gaziz.birgram.features.searchChats.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.features.searchChats.domain.model.ChatData

interface SearchChatsRepository {
    val searchedChats: StateFlow<Map<Long, ChatData>>
    fun searchLocal(
        query: String,
        limit: Int
    )
    fun downloadChatIcon(
        chatId: Long,
        fileId: Int
    )
}