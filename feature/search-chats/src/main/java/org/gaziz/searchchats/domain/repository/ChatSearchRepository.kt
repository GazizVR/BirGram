package org.gaziz.searchchats.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.searchchats.domain.model.SearchedItem

interface ChatSearchRepository {
    val chats: StateFlow<Map<Long, SearchedItem>>
    fun replace(newChats: Map<Long, SearchedItem>)
}