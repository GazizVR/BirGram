package org.gaziz.birgram.core.telegram.chats.api.model

sealed interface DraftMessageContent {
    data class Text(
        val text: String,
        val clearDraft: Boolean
    ): DraftMessageContent
    object Other: DraftMessageContent
}