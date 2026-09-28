package org.gaziz.birgram.feature.chat.ui.mapper

import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.group.GroupMemberStatus
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.ui.model.ChatTypeUiState
import org.gaziz.birgram.core.telegram.ui.mapper.ChatAvatarUiMapper
import org.gaziz.birgram.feature.chat.ui.model.ChatUiState
import javax.inject.Inject

class ChatUiMapper @Inject constructor(
    private val userService: UserService,
    private val chatAvatarUiMapper: ChatAvatarUiMapper,
    private val groupService: GroupService,
) {
    fun map(chat: Chat): ChatUiState {
        val chatType = chat.type
        val isDeleted =
            chatType is ChatType.Private &&
            userService.users.value[chatType.userId]?.type == UserType.Deleted &&
            userService.users.value[chatType.userId]?.type == UserType.Unknown
        val avatar = chatAvatarUiMapper(chat)
        var canSendTextMessages = chat.permissions.canSendBasicMessages
        val typeInfo: ChatTypeUiState? = when(val type = chat.type) {
            is ChatType.BasicGroup -> {
                val group = groupService.basicGroups.value[type.groupId]
                canSendTextMessages =
                    chat.permissions.canSendBasicMessages ||
                            group?.memberStatus is GroupMemberStatus.Creator ||
                            (group?.memberStatus is GroupMemberStatus.Admin &&
                                    (group.memberStatus as GroupMemberStatus.Admin).canPostMessages)
                if(group != null) {
                    ChatTypeUiState.BasicGroup(
                        memberCount = group.memberCount,
                    )
                } else {
                    null
                }
            }
            is ChatType.SuperGroup -> {
                val group = groupService.superGroups.value[type.groupId]
                canSendTextMessages =
                    chat.permissions.canSendBasicMessages ||
                            group?.memberStatus is GroupMemberStatus.Creator ||
                            (group?.memberStatus is GroupMemberStatus.Admin &&
                                    (group.memberStatus as GroupMemberStatus.Admin).canPostMessages)
                if(group != null) {
                    ChatTypeUiState.SuperGroup(
                        memberCount = group.memberCount,
                        isChannel = type.isChannel
                    )
                } else {
                    null
                }
            }
            is ChatType.Private -> {
                val user = userService.users.value[type.userId]
                if(user != null) {
                    ChatTypeUiState.User(
                        status = user.status,
                        isBot = user.type is UserType.Bot
                    )
                } else {
                    null
                }
            }
            is ChatType.Secret -> {
                val user = userService.users.value[type.userId]
                if(user != null) {
                    ChatTypeUiState.User(
                        status = user.status,
                        isBot = user.type is UserType.Bot
                    )
                } else {
                    null
                }
            }
            else -> null
        }
        var draftMessageText = ""
        val draftMsg = chat.draftMessage
        if(
            draftMsg != null &&
            draftMsg.content is DraftMessageContent.Text
        ) {
            draftMessageText = (draftMsg.content as DraftMessageContent.Text).text
        }
        return ChatUiState(
            id = chat.id,
            title = chat.title,
            avatar = avatar,
            isDeleted = isDeleted,
            typeInfo = typeInfo,
            draftText = draftMessageText,
            canSendTextMessages = canSendTextMessages,
            unreadCount = if(chat.unreadCount > 0) chat.unreadCount else null,
            lastMessage = chat.lastMessage
        )
    }
}