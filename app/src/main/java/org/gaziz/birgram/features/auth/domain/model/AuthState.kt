package org.gaziz.birgram.features.auth.domain.model

sealed class AuthState {
    object WaitParams: AuthState()
    object WaitPhoneNumber: AuthState()
    data class WaitCode(val codeInfo: AuthCodeInfo): AuthState()
    data class WaitPassword(val passwordInfo: AuthPasswordInfo): AuthState()
    object Ready: AuthState()

    data class Other(val state: String): AuthState()

    object LoggingOut: AuthState()
    object Closed: AuthState()
}