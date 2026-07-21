package org.gaziz.birgram.core.telegram.internal.updaters

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPosition
import org.gaziz.birgram.core.telegram.internal.mapper.toAccentColor
import org.gaziz.birgram.core.telegram.internal.mapper.toChat
import org.gaziz.birgram.core.telegram.internal.mapper.toChatPosition
import org.gaziz.birgram.core.telegram.internal.mapper.toDraftMessage
import org.gaziz.birgram.core.telegram.internal.mapper.toMessage
import org.gaziz.birgram.core.telegram.internal.mapper.toPermissions
import org.gaziz.birgram.core.telegram.internal.mapper.toPhotoInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatUpdater @Inject constructor(
    private val chatService: ChatService
) {
    fun onLoggingOut() {
        chatService.updateChats { emptyMap() }
    }

    fun onNewUpdate(u: TdApi.UpdateNewChat){
        chatService.updateChats { map ->
            map + (u.chat.id to u.chat.toChat())
        }
    }
    fun onPositionUpdate(u: TdApi.UpdateChatPosition){
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            if (u.position.order == 0L) {
                old - u.chatId
            } else {
                val positions = chat.positions.filter {
                    it.listType != u.position.toChatPosition().listType
                } + u.position.toChatPosition()
                val newChat = chat.copy(positions = positions)
                old + (u.chatId to newChat)
            }
        }
    }
    fun onLastMsgUpdate(u: TdApi.UpdateChatLastMessage){
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val positions = mutableListOf<ChatPosition>().apply {
                u.positions.forEach { pos ->
                    add(pos.toChatPosition())
                }
            }.toList()
            val newChat = chat.copy(
                positions = positions,
                lastMessage = u.lastMessage?.toMessage()
            )
            old + (u.chatId to newChat)
        }
    }
    fun onDraftMsgUpdate(u: TdApi.UpdateChatDraftMessage) {
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val positions = mutableListOf<ChatPosition>().apply {
                u.positions.forEach { pos ->
                    add(pos.toChatPosition())
                }
            }.toList()
            val newChat = chat.copy(
                draftMessage = u.draftMessage.toDraftMessage(),
                positions = positions
            )
            old + (u.chatId to newChat)
        }
    }
    fun onPermissionsUpdate(u: TdApi.UpdateChatPermissions) {
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val newChat = chat.copy(permissions = u.permissions.toPermissions())
            old + (u.chatId to newChat)
        }
    }
    fun onChatAccentColorsUpdate(u: TdApi.UpdateChatAccentColors){
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val newChat = chat.copy(accentColorId = u.accentColorId)
            old + (u.chatId to newChat)
        }
    }
    fun onAccentColorsUpdate(u: TdApi.UpdateAccentColors) {
        chatService.updateAccentColors { old ->
            old.toMutableMap().apply {
                u.colors.forEach { color ->
                    put(color.id,color.toAccentColor())
                }
            }.toMap()
        }
    }

    fun onTitleUpdate(u: TdApi.UpdateChatTitle){
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val newChat = chat.copy(title = u.title)
            old + (u.chatId to newChat)
        }
    }
    fun onPhotoUpdate(u: TdApi.UpdateChatPhoto){
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val newChat = chat.copy(photo = u.photo.toPhotoInfo())
            old + (u.chatId to newChat)
        }
    }

    fun onInboxUpdate(u: TdApi.UpdateChatReadInbox){
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val newChat = chat.copy(unreadCount = u.unreadCount)
            old + (u.chatId to newChat)
        }
    }
    fun onMentionCountUpdate(u: TdApi.UpdateChatUnreadMentionCount){
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val newChat = chat.copy(mentionCount = u.unreadMentionCount)
            old + (u.chatId to newChat)
        }
    }
    fun onReactionCountUpdate(u: TdApi.UpdateChatUnreadReactionCount){
        chatService.updateChats { old ->
            val chat = old[u.chatId] ?: return@updateChats old
            val newChat = chat.copy(reactionCount = u.unreadReactionCount)
            old + (u.chatId to newChat)
        }
    }
}