package org.gaziz.birgram.core.telegram.internal.updaters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.mapper.copy
import org.gaziz.birgram.core.telegram.internal.ClientManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatUpdater @Inject constructor(
    private val manager: ClientManager
) {
    private val _chats = MutableStateFlow<Map<Long, TdApi.Chat>>(emptyMap())
    val chats = _chats.asStateFlow()

    fun onLoggingOut() {
        _chats.update { emptyMap() }
    }

    fun onNewUpdate(u: TdApi.UpdateNewChat){
        _chats.update { map ->
            map + (u.chat.id to u.chat)
        }
    }
    fun onPositionUpdate(u: TdApi.UpdateChatPosition){
        _chats.update { old ->
            val chat = old[u.chatId] ?: return@update old
            if (u.position.order == 0L) {
                old - u.chatId
            } else {
                val positions = chat.positions.filter { it.list != u.position.list } + u.position
                val newChat = chat.copy(positions = positions.toTypedArray())
                old + (u.chatId to newChat)
            }
        }
    }
    fun onLastMsgUpdate(u: TdApi.UpdateChatLastMessage){
        _chats.update { old ->
            val chat = old[u.chatId] ?: return@update old
            val newChat = chat.copy(
                positions = u.positions,
                lastMessage = u.lastMessage
            )
            old + (u.chatId to newChat)
        }
    }
    fun onDraftMsgUpdate(u: TdApi.UpdateChatDraftMessage) {
        _chats.update { old ->
            val chat = old[u.chatId] ?: return@update old
            val newChat = chat.copy(
                draftMessage = u.draftMessage,
                positions = u.positions
            )
            old + (u.chatId to newChat)
        }
    }

    fun onTitleUpdate(u: TdApi.UpdateChatTitle){
        _chats.update { old ->
            val chat = old[u.chatId] ?: return@update old
            val newChat = chat.copy(title = u.title)
            old + (u.chatId to newChat)
        }
    }
    fun onPhotoUpdate(u: TdApi.UpdateChatPhoto){
        _chats.update { old ->
            val chat = old[u.chatId] ?: return@update old
            val newChat = chat.copy(photo = u.photo)
            old + (u.chatId to newChat)
        }
    }

    fun onInboxUpdate(u: TdApi.UpdateChatReadInbox){
        _chats.update { old ->
            val chat = old[u.chatId] ?: return@update old
            val newChat = chat.copy(unreadCount = u.unreadCount)
            old + (u.chatId to newChat)
        }
    }
    fun onMentionCountUpdate(u: TdApi.UpdateChatUnreadMentionCount){
        _chats.update { old ->
            val chat = old[u.chatId] ?: return@update old
            val newChat = chat.copy(unreadMentionCount = u.unreadMentionCount)
            old + (u.chatId to newChat)
        }
    }
    fun onReactionCountUpdate(u: TdApi.UpdateChatUnreadReactionCount){
        _chats.update { old ->
            val chat = old[u.chatId] ?: return@update old
            val newChat = chat.copy(unreadReactionCount = u.unreadReactionCount)
            old + (u.chatId to newChat)
        }
    }

    fun downloadChatPhotoSmall(
        chatId: Long,
        fileId: Int
    ) {
        manager.sendRequest(
            TdApi.DownloadFile().apply {
                this.fileId = fileId
                this.priority = 32
                this.limit = 0
                this.offset = 0
                this.synchronous = true
            },
            {},
            { obj ->
                if(obj is TdApi.File) {
                    _chats.update { old ->
                        val chat = old[chatId] ?: return@update old
                        val chatPhoto = chat.photo
                        var photo: TdApi.ChatPhotoInfo = TdApi.ChatPhotoInfo().apply {
                            this.small = obj
                        }
                        if(chatPhoto != null) { photo = chatPhoto.copy(small = obj) }
                        old + (chatId to chat.copy(photo = photo))
                    }
                }
            }
        )
    }

}