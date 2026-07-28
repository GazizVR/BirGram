package org.gaziz.birgram.features.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.group.GroupMemberStatus
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.api.usecase.GetChatAvatar
import org.gaziz.birgram.core.ui.model.ChatTypeInfo
import org.gaziz.birgram.features.chat.domain.usecase.GetChatById
import org.gaziz.birgram.features.chat.domain.usecase.GetChatMessages
import org.gaziz.birgram.features.chat.domain.usecase.LoadChatMessages
import org.gaziz.birgram.features.chat.ui.mapper.formatMonthDay
import org.gaziz.birgram.features.chat.ui.mapper.toTimeString
import org.gaziz.birgram.features.chat.ui.model.ChatUiState
import org.gaziz.birgram.features.chat.ui.model.MessageUiState
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatById: GetChatById,
    private val getChatMessages: GetChatMessages,
    private val chatService: ChatService,
    private val loadChatMessages: LoadChatMessages,
    private val userService: UserService,
    private val getChatAvatar: GetChatAvatar,
    private val groupService: GroupService
): ViewModel() {
    private var isLoading = false
    fun openChat(
        chatId: Long,
    ) {
        chatService.openChat(chatId) {
            isLoading = true
            loadChatMessages(chatId, onResp = { isLoading = false } )
        }
    }
    fun closeChat(
        chatId: Long,
    ) {
        chatService.closeChat(chatId)
    }
    val chat: (Long) -> StateFlow<ChatUiState?> = {
        getChatById(it).map { chat ->
            chat ?: return@map null
            val isDeleted =
                chat.type is ChatType.Private &&
                userService.users.value[chat.type.userId]?.type == UserType.Deleted &&
                userService.users.value[chat.type.userId]?.type == UserType.Unknown
            val avatar = getChatAvatar(chat)
            var canSendTextMessages = chat.permissions.canSendBasicMessages
            val typeInfo: ChatTypeInfo? = when(val type = chat.type) {
                is ChatType.BasicGroup -> {
                    val group = groupService.basicGroups.value[type.groupId]
                    canSendTextMessages =
                        chat.permissions.canSendBasicMessages ||
                        group?.memberStatus is GroupMemberStatus.Creator ||
                        (group?.memberStatus is GroupMemberStatus.Admin && group.memberStatus.canPostMessages)
                    if(group != null) {
                        ChatTypeInfo.BasicGroup(
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
                                (group?.memberStatus is GroupMemberStatus.Admin && group.memberStatus.canPostMessages)
                    if(group != null) {
                        ChatTypeInfo.SuperGroup(
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
                        ChatTypeInfo.User(
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
                        ChatTypeInfo.User(
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
            if(
                chat.draftMessage != null &&
                chat.draftMessage.content is DraftMessageContent.Text &&
                !chat.draftMessage.content.clearDraft
            ) {
                draftMessageText = chat.draftMessage.content.text
            }
            ChatUiState(
                title = chat.title,
                avatar = avatar,
                isDeleted = isDeleted,
                typeInfo = typeInfo,
                draftText = draftMessageText,
                canSendTextMessages = canSendTextMessages,
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
    }
    val messages: (Long) -> StateFlow<Map<String, List<MessageUiState>>> = {
        getChatMessages(it).map { map ->
            map.entries.associate { (key,value) ->
                val messages = value.map { msg ->
                    MessageUiState(
                        id = msg.id,
                        content = msg.content,
                        isOutgoing = msg.isOutgoing,
                        date = msg.date.toTimeString()
                    )
                }
                key.formatMonthDay() to messages
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyMap()
        )
    }
    fun loadMessages(
        chatId: Long,
        fromMessageId: Long
    ){
        if(isLoading) return
        isLoading = true
        loadChatMessages(
            chatId,
            fromMessageId
        ) {
            isLoading = false
        }
    }
}