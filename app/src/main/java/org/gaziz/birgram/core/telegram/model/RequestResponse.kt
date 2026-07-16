package org.gaziz.birgram.core.telegram.model

sealed class RequestResponse {
    object OK: RequestResponse()
    data class Error(
        val code: Int,
        val message: String
    ): RequestResponse()
}