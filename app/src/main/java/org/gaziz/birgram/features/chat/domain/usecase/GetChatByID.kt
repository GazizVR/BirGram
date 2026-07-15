package org.gaziz.birgram.features.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.domain.repository.EventLoopRepository
import org.gaziz.birgram.features.chatList.domain.model.chat.ChatData
import javax.inject.Inject

class GetChatByID @Inject constructor(
    private val eventLoopRepository: EventLoopRepository
) {
    operator fun invoke(chatId: Long): Flow<ChatData?> {
        return eventLoopRepository.chatList.map {
            it[chatId]
        }
    }
}