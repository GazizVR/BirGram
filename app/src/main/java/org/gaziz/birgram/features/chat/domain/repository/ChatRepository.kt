package org.gaziz.birgram.features.chat.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.features.chat.domain.model.ChatData
import org.gaziz.birgram.features.chat.domain.model.MessageData

interface ChatRepository {
    val chats: StateFlow<Map<Long, ChatData>>
    val messages: StateFlow<Map<Long, MessageData>>
    fun setMessages(
        updFun: (Map<Long, MessageData>) -> Map<Long, MessageData>
    )
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