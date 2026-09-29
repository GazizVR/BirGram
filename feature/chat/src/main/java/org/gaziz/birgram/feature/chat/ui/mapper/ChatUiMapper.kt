package org.gaziz.birgram.feature.chat.ui.mapper

import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.group.BasicGroup
import org.gaziz.birgram.core.telegram.api.model.group.GroupMemberStatus
import org.gaziz.birgram.core.telegram.api.model.group.SuperGroup
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.ui.model.ChatTypeUiState
import org.gaziz.birgram.core.telegram.ui.provider.ChatAvatarProvider
import org.gaziz.birgram.feature.chat.ui.model.ChatUiState
import javax.inject.Inject

class ChatUiMapper @Inject constructor(
    private val chatAvatarProvider: ChatAvatarProvider,
) {
    operator fun invoke(
        chat: Chat,
        usersById: Map<Long, User>,
        basicGroupsById: Map<Long, BasicGroup>,
        superGroupsById: Map<Long, SuperGroup>,
    ): ChatUiState {
        val isDeleted = run {
            val chatType = chat.type
            chatType is ChatType.Private &&
            usersById[chatType.userId]?.type == UserType.Deleted &&
            usersById[chatType.userId]?.type == UserType.Unknown
        }
        val avatar = chatAvatarProvider(chat,usersById)
        var canSendTextMessages = chat.permissions.canSendBasicMessages
        val typeInfo: ChatTypeUiState? = when(val type = chat.type) {
            is ChatType.BasicGroup -> {
                val group = basicGroupsById[type.groupId]
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
                val group = superGroupsById[type.groupId]
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
                val user = usersById[type.userId]
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
                val user = usersById[type.userId]
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