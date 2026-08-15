package org.gaziz.telegram.api.model.group

data class SuperGroup(
    val id: Long,
    val isChannel: Boolean,
    val memberCount: Int,
    val memberStatus: GroupMemberStatus
)
