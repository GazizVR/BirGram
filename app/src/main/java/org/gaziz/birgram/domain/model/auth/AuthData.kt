package org.gaziz.birgram.domain.model.auth

sealed class AuthData {
    object Parameters : AuthData()
    data class PhoneNumber(val phoneNumber: String) : AuthData()
    data class Code(val code: String) : AuthData()
    data class Password(val password: String) : AuthData()
    data class Email(val email: String) : AuthData()
    data class EmailCode(val emailCode: String) : AuthData()
    data class Registration(
        val firstName: String,
        val lastName: String,
        val disableNotification: Boolean
    ) : AuthData()
}