package org.gaziz.birgram.features.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
import org.gaziz.birgram.features.chat.domain.model.ChatData
import javax.inject.Inject

class GetChatByID @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: Long): Flow<ChatData?> {
        return chatRepository.chats.map {
            it[chatId]
        }
    }
}