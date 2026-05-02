package org.gaziz.birgram.domain.model.auth

sealed class AuthState {
    object Parameters: AuthState()
    object PhoneNumber: AuthState()
    object Code: AuthState()
    object Password: AuthState()
    object Email: AuthState()
    object EmailCode: AuthState()
    object Registration: AuthState()
    object Ready: AuthState()
}