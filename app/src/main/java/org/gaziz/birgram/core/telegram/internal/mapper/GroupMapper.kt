package org.gaziz.birgram.core.telegram.internal.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.group.BasicGroup
import org.gaziz.birgram.core.telegram.api.model.group.GroupMemberStatus
import org.gaziz.birgram.core.telegram.api.model.group.SuperGroup

fun TdApi.ChatMemberStatus.toMemberStatus(): GroupMemberStatus {
    return when(val status = this) {
        is TdApi.ChatMemberStatusCreator -> GroupMemberStatus.Creator
        is TdApi.ChatMemberStatusMember -> GroupMemberStatus.Member
        is TdApi.ChatMemberStatusBanned -> GroupMemberStatus.Banned
        is TdApi.ChatMemberStatusRestricted -> GroupMemberStatus.Restricted(status.permissions.toPermissions())
        is TdApi.ChatMemberStatusAdministrator -> GroupMemberStatus.Admin(status.rights.canPostMessages)
        else -> GroupMemberStatus.Left
    }
}

fun TdApi.BasicGroup.toBasicGroup(): BasicGroup {
    return BasicGroup(
        id = this.id,
        memberCount = this.memberCount,
        status = this.status.toMemberStatus(),
        upgradedSuperGroupId = this.upgradedToSupergroupId
    )
}

fun TdApi.Supergroup.toSuperGroup(): SuperGroup {
    return SuperGroup(
        id = this.id,
        memberCount = this.memberCount,
        status = this.status.toMemberStatus(),
        isChannel = this.isChannel
    )
}
