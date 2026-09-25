package org.gaziz.telegram.api

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.telegram.api.model.ResponseData
import org.gaziz.telegram.api.model.message.DraftMessage
import org.gaziz.telegram.api.model.message.Message
import org.gaziz.telegram.api.model.message.MessageProperties

interface MessageService {
    val messageProperties: StateFlow<Map<Long, MessageProperties>>
    fun setMessageProperties(
        chatId: Long,
        messageId: Long
    )
    val messages: StateFlow<Map<Long, Message>>
    fun updateMessages(updFun: (Map<Long, Message>) -> (Map<Long, Message>))
    fun getChatHistory(
        chatId: Long,
        fromMessage: Long,
        onError: (ResponseData.Error) -> Unit,
        onResult: () -> Unit
    )
    fun sendMessage(
        chatId: Long,
        content: String
    )
    fun setDraftMessage(
        chatId: Long,
        draftMessage: DraftMessage
    )
    fun deleteMessages(
        chatId: Long,
        msgIds: LongArray,
        forAll: Boolean
    )
}