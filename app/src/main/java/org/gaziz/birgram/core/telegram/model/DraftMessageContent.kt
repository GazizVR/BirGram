package org.gaziz.birgram.core.telegram.model

sealed interface DraftMessageContent {
    data class Text(
        val text: String,
        val clearDraft: Boolean
    ): DraftMessageContent
    object Other: DraftMessageContent
}