package org.gaziz.telegram.api.model.message

sealed interface DraftMessageContent {
    data class Text(val text: String): DraftMessageContent
    object Other: DraftMessageContent
}