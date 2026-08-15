package org.gaziz.birgram.features.chat.domain.usecase

import org.gaziz.telegram.api.MessageService
import javax.inject.Inject

class LoadChatMessages @Inject constructor(
    private val messageService: MessageService
) {
    operator fun invoke(
        chatId: Long,
        fromMsgId: Long = 0,
        onResp: () -> Unit
    ) {
        messageService.getChatHistory(
            chatId,
            fromMsgId,
            { onResp() },
            { onResp() },
        )
    }
}