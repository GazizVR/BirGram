package org.gaziz.birgram.domain.model.auth

sealed class AuthState {
    object WaitParams: AuthState()
    object WaitPhoneNumber: AuthState()
    data class WaitCode(val codeInfo: AuthCodeInfo): AuthState()
    data class WaitPassword(val passwordInfo: AuthPasswordInfo): AuthState()
    object Ready: AuthState()

    data class Other(val state: String): AuthState()
}