package org.gaziz.birgram.features.searchChats.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gaziz.birgram.features.searchChats.domain.model.SearchedItem
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import javax.inject.Inject

class ChatSearchRepositoryImpl @Inject constructor(): ChatSearchRepository {
    private val _chats = MutableStateFlow<Map<Long, SearchedItem>>(emptyMap())
    override val chats: StateFlow<Map<Long, SearchedItem>> = _chats.asStateFlow()

    override fun replace(newChats: Map<Long, SearchedItem>) {
        _chats.update { newChats }
    }
}