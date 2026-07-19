package org.gaziz.birgram.core.telegram.api.model.chat

sealed interface ChatType {
    data class BasicGroup(
        val groupId: Long,
    ) : ChatType
    data class SuperGroup(
        val groupId: Long,
        val isChannel: Boolean
    ): ChatType
    data class Private(
        val userId: Long
    ): ChatType
    data class Secret(
        val chatId: Int,
        val userId: Long
    ): ChatType
    object Other: ChatType
}