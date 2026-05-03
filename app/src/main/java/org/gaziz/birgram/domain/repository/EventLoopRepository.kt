package org.gaziz.birgram.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.domain.model.auth.AuthState

interface EventLoopRepository {
    val authState: StateFlow<AuthState>
    val errorMessage: StateFlow<String?>
    fun setErrorMessage(errorMessage: String)
    fun createEventLoop()
}