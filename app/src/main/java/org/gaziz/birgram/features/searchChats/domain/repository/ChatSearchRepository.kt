package org.gaziz.birgram.features.searchChats.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.features.searchChats.domain.model.SearchedItem

interface ChatSearchRepository {
    val chats: StateFlow<Map<Long, SearchedItem>>
    fun replace(newChats: Map<Long, SearchedItem>)
}