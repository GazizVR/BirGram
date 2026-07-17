package org.gaziz.birgram.features.chat.domain.repository

import org.gaziz.birgram.core.telegram.model.DraftMessage
import org.gaziz.birgram.features.chat.domain.model.MessageData

interface ChatRepository {
    fun openChat(
        chatId: Long,
        onOK: () -> Unit
    )
    fun closeChat(chatId: Long)
    fun loadMessages(
        chatId: Long,
        fromMessageId: Long = 0,
        onErr: () -> Unit
    )
    fun sendMessage(
        chatId: Long,
        content: String,
        onMessage: (MessageData?) -> Unit
    )
    fun setDraftMessage(
        chatId: Long,
        draftMessage: DraftMessage
    )
}