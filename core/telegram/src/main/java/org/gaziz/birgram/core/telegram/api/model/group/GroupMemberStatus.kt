package org.gaziz.birgram.core.telegram.api.model.group

import org.gaziz.birgram.core.telegram.api.model.chat.ChatPermissions

sealed interface GroupMemberStatus {
    object Left: GroupMemberStatus
    data class Restricted(val permissions: ChatPermissions): GroupMemberStatus
    object Member: GroupMemberStatus
    object Creator: GroupMemberStatus
    data class Admin(val canPostMessages: Boolean): GroupMemberStatus
    object Banned: GroupMemberStatus
}