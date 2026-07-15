package org.gaziz.birgram.core.telegram.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatData
import org.gaziz.birgram.core.telegram.domain.model.message.MessageData
import org.gaziz.birgram.features.auth.domain.model.AuthState

interface EventLoopRepository {
    val authState: StateFlow<AuthState>
    val errorMessage: StateFlow<String?>
    fun setErrorMessage(errorMessage: String?)
    val chatList: StateFlow<Map<Long, ChatData>>
    val messages: StateFlow<Map<Long, MessageData>>
    fun setMessages(
        updFun: (Map<Long, MessageData>) -> Map<Long, MessageData>
    )
    fun createEventLoop()
    fun restartAuth()
    fun logOut(onOk: () -> Unit)
}