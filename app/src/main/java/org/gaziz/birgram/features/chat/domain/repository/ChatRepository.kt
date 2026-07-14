package org.gaziz.birgram.features.chat.domain.repository

import org.gaziz.birgram.core.telegram.domain.model.message.MessageData

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
    fun sendMessage(
        chatId: Long,
        content: String,
        onMessage: (MessageData?) -> Unit
    )
}