package org.gaziz.birgram.features.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.features.chat.domain.model.MessageData
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
import java.time.LocalDate
import javax.inject.Inject

class GetChatMessages @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(
        chatId: Long,
    ): Flow<Map<LocalDate,List<MessageData>>> {
        return chatRepository.messages.map { map ->
            map.values.asSequence()
                .filter { it.chatId == chatId }
                .sortedByDescending { it.date }
                .groupBy { it.date.toLocalDate() }
        }
    }
}