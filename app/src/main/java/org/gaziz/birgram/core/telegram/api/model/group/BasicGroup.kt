package org.gaziz.birgram.core.telegram.api.model.group

data class BasicGroup(
    val id: Long,
    val memberCount: Int,
    val status: GroupMemberStatus,
    val upgradedSuperGroupId: Long
)
