package org.gaziz.birgram.core.telegram.api.model.message

sealed interface DraftMessageContent {
    data class Text(
        val text: String,
        val clearDraft: Boolean
    ): DraftMessageContent
    object Other: DraftMessageContent
}