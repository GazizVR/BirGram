package org.gaziz.telegram.api.model

sealed interface ResponseData {
    object OK: ResponseData
    data class Error(
        val code: Int,
        val message: String
    ): ResponseData
}