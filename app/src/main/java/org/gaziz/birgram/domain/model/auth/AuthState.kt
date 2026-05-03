package org.gaziz.birgram.domain.model.auth

sealed class AuthState {
    object WaitParams: AuthState()
    object WaitPhoneNumber: AuthState()
    object WaitCode: AuthState()
    object WaitPassword: AuthState()
    object Ready: AuthState()

    data class Other(val state: String): AuthState()
}