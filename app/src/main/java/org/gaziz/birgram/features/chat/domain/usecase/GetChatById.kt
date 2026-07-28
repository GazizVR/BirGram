package org.gaziz.birgram.features.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import javax.inject.Inject

class GetChatById @Inject constructor(
    private val chatService: ChatService,
) {
    operator fun invoke(
        id: Long
    ): Flow<Chat?> {
        return chatService.chats.map { it[id] }
    }
}