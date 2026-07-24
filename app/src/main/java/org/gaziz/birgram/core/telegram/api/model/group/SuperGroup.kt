package org.gaziz.birgram.core.telegram.api.model.group

data class SuperGroup(
    val id: Long,
    val isChannel: Boolean,
    val memberCount: Int,
    val status: GroupMemberStatus
)
