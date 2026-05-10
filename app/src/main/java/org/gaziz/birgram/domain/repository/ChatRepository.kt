package org.gaziz.birgram.domain.repository

import org.gaziz.birgram.domain.model.message.MessageData

interface ChatRepository {
    fun openChat(
        chatId: Long,
        onOK: () -> Unit
    )
    fun closeChat(chatId: Long)
    fun getChatMessages(
        chatId: Long,
        fromMessage: Long,
        onMessages: (List<MessageData>) -> Unit
    )
}