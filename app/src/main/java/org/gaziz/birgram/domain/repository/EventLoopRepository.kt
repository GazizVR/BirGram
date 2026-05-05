package org.gaziz.birgram.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.domain.model.chatList.ChatData

interface EventLoopRepository {
    val authState: StateFlow<AuthState>
    val errorMessage: StateFlow<String?>
    val chatList: StateFlow<Map<Long,ChatData>>
    fun setErrorMessage(errorMessage: String?)
    fun createEventLoop()
    fun restartAuth()
}