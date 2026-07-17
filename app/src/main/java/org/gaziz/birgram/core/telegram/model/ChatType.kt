package org.gaziz.birgram.core.telegram.model

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
        val userId: Long,
        val userStatus: UserStatus? = null
    ): ChatType
    object Other: ChatType
}