package org.gaziz.birgram.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.domain.model.chat.ChatData

interface SearchChatsRepository {
    val searchedChats: StateFlow<Map<Long, ChatData>>
    fun updateSearchChats(chat: ChatData?)
    fun searchLocal(
        query: String,
        limit: Int,
        onResult: (List<Long>) -> Unit
    )
    fun downloadPhoto(fileId: Int)
}