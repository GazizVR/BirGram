package org.gaziz.birgram.features.searchChats.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.api.model.chat.Chat

interface ChatSearchRepository {
    val chats: StateFlow<Map<Long, Chat>>
    fun replace(newChats: Map<Long,Chat>)
}