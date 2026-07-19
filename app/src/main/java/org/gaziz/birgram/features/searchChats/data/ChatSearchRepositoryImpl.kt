package org.gaziz.birgram.features.searchChats.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import javax.inject.Inject

class ChatSearchRepositoryImpl @Inject constructor(): ChatSearchRepository {
    private val _chats = MutableStateFlow<Map<Long,Chat>>(emptyMap())
    override val chats: StateFlow<Map<Long, Chat>> = _chats.asStateFlow()

    override fun replace(newChats: Map<Long, Chat>) {
        _chats.update { newChats }
    }
}