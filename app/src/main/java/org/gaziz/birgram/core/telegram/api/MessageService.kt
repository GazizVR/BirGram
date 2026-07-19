package org.gaziz.birgram.core.telegram.api

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.Message

interface MessageService {
    val messages: StateFlow<Map<Long,Message>>
    fun updateMessages(updFun: (Map<Long,Message>) -> (Map<Long,Message>))
    fun getMessages(
        chatId: Long,
        fromMessage: Long = 0,
        onError: () -> Unit
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