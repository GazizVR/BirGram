package org.gaziz.birgram.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.domain.model.chat.ChatData
import org.gaziz.birgram.domain.model.message.MessageData

interface EventLoopRepository {
    val authState: StateFlow<AuthState>
    val errorMessage: StateFlow<String?>
    fun setErrorMessage(errorMessage: String?)
    val chatList: StateFlow<Map<Long,ChatData>>
    val messages: StateFlow<Map<Long, MessageData>>
    fun setMessages(map: Map<Long, MessageData>)
    fun createEventLoop()
    fun restartAuth()
}