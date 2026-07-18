package org.gaziz.birgram.features.chat.domain.repository

import org.gaziz.birgram.core.telegram.chats.api.model.DraftMessage
import org.gaziz.birgram.core.telegram.messages.api.model.Message

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
        onMessage: (Message?) -> Unit
    )
    fun setDraftMessage(
        chatId: Long,
        draftMessage: DraftMessage
    )
}