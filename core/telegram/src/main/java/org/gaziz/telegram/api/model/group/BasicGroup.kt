package org.gaziz.telegram.api.model.group

data class BasicGroup(
    val id: Long,
    val memberCount: Int,
    val memberStatus: GroupMemberStatus,
    val upgradedSuperGroupId: Long
)
