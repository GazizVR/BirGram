package org.gaziz.birgram.features.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.data.source.TelegramMessage
import org.gaziz.birgram.features.chat.data.mapper.toMessageData
import org.gaziz.birgram.features.chat.domain.model.MessageData
import java.time.LocalDate
import javax.inject.Inject

class GetChatMessages @Inject constructor(
    private val tgMessage: TelegramMessage
) {
    operator fun invoke(
        chatId: Long,
    ): Flow<Map<LocalDate,List<MessageData>>> {
        return tgMessage.messages.map { map ->
            map
                .asSequence()
                .map { it.value.toMessageData() }
                .filter { it.chatId == chatId }
                .sortedByDescending { it.date }
                .groupBy { it.date.toLocalDate() }
        }
    }
}