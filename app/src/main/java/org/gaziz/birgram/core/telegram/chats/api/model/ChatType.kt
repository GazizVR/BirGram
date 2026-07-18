package org.gaziz.birgram.core.telegram.chats.api.model

sealed interface ChatType {
    data class BasicGroup(
        val groupId: Long,
        val memberCont: Int = 0
    ) : ChatType
    data class SuperGroup(
        val groupId: Long,
        val isChannel: Boolean,
        val memberCont: Int = 0,
        val canSendMessages: Boolean = false
    ): ChatType
    data class Private(
        val userId: Long
    ): ChatType
    object Other: ChatType
}