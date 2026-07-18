package org.gaziz.birgram.core.telegram.api.model.chat

sealed interface DraftMessageContent {
    data class Text(
        val text: String,
        val clearDraft: Boolean
    ): DraftMessageContent
    object Other: DraftMessageContent
}