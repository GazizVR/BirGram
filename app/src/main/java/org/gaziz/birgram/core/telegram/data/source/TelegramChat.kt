package org.gaziz.birgram.core.telegram.data.source

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramChat @Inject constructor() {
    private val _chats = MutableStateFlow<Map<Long, TdApi.Chat>>(emptyMap())
    val chats = _chats.asStateFlow()

    fun onNewUpdate(u: TdApi.UpdateNewChat){
        _chats.update { map ->
            map.toMutableMap().apply { put(u.chat.id,u.chat) }.toMap()
        }
    }
    fun onTitleUpdate(u: TdApi.UpdateChatTitle){
        _chats.update { map ->
            map.toMutableMap().apply {
                get(u.chatId)?.let { chat ->
                    chat.title = u.title
                    put(u.chatId,chat)
                }
            }.toMap()
        }
    }
    fun onPhotoUpdate(u: TdApi.UpdateChatPhoto){
        _chats.update { map ->
            map.toMutableMap().apply {
                get(u.chatId)?.let { chat ->
                    chat.photo = u.photo
                    put(u.chatId,chat)
                }
            }.toMap()
        }
    }
    fun onPositionUpdate(u: TdApi.UpdateChatPosition){
        _chats.update { map ->
            map.toMutableMap().apply {
                get(u.chatId)?.let { chat ->
                    if (u.position.order == 0L) {
                        remove(u.chatId)
                    } else {
                        val positions = chat.positions.filter { it.list != u.position.list } + u.position
                        chat.positions = positions.toTypedArray()
                        put(u.chatId, chat)
                    }
                }
            }.toMap()
        }
    }
    fun onLastMsgUpdate(u: TdApi.UpdateChatLastMessage){
        _chats.update { map ->
            map.toMutableMap().apply {
                get(u.chatId)?.let { chat ->
                    chat.lastMessage = u.lastMessage ?: chat.lastMessage
                    chat.positions = u.positions
                    put(u.chatId, chat)
                }
            }.toMap()
        }
    }
    fun onInboxUpdate(u: TdApi.UpdateChatReadInbox){
        _chats.update { map ->
            map.toMutableMap().apply {
                get(u.chatId)?.let { chat ->
                    chat.unreadCount = u.unreadCount
                    put(u.chatId,chat)
                }
            }.toMap()
        }
    }
    fun onMentionCountUpdate(u: TdApi.UpdateChatUnreadMentionCount){
        _chats.update { map ->
            map.toMutableMap().apply {
                get(u.chatId)?.let { chat ->
                    chat.unreadMentionCount = u.unreadMentionCount
                    put(u.chatId,chat)
                }
            }.toMap()
        }
    }
    fun onReactionCountUpdate(u: TdApi.UpdateChatUnreadReactionCount){
        _chats.update { map ->
            map.toMutableMap().apply {
                get(u.chatId)?.let { chat ->
                    chat.unreadReactionCount = u.unreadReactionCount
                    put(u.chatId,chat)
                }
            }.toMap()
        }
    }

}