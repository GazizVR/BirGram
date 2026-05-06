package org.gaziz.birgram.domain.model.chatList

sealed class UpdateChat {
    data class LastMessage(
        val chatId: Long,
        val lastMessage: MessageData,
        val positions: List<ChatPosition>
    ): UpdateChat()
}