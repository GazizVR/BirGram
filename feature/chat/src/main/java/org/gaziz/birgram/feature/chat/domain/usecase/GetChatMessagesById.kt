package org.gaziz.birgram.feature.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.api.model.message.Message
import javax.inject.Inject

class GetChatMessagesById @Inject constructor(
    private val messageService: MessageService
) {
    operator fun invoke(
        chatId: Long
    ): Flow<Map<Long, Message>> {
        return messageService.messages.map {
            it.filterValues { message -> message.chatId == chatId }
        }
    }
}