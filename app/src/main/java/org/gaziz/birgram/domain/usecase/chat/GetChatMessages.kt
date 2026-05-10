package org.gaziz.birgram.domain.usecase.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.domain.model.message.MessageData
import org.gaziz.birgram.domain.repository.EventLoopRepository
import javax.inject.Inject

class GetChatMessages @Inject constructor(
    private val eventLoopRepository: EventLoopRepository
) {
    operator fun invoke(
        chatId: Long,
    ): Flow<List<MessageData>> {
        return eventLoopRepository.messages.map { msgs ->
            msgs
                .map { it.value }
                .filter { it.chatId == chatId }
                .sortedByDescending { it.date }
        }
    }
}