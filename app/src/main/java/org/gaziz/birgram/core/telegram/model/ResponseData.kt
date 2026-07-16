package org.gaziz.birgram.core.telegram.model

sealed class ResponseData {
    object OK: ResponseData()
    data class Error(
        val code: Int,
        val message: String
    ): ResponseData()
}