package org.gaziz.birgram.domain.model.chatList

sealed class RequestResponse {
    object OK: RequestResponse()
    data class Error(
        val code: Int,
        val message: String
    ): RequestResponse()
}