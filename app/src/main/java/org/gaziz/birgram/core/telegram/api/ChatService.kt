package org.gaziz.birgram.core.telegram.api

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.api.model.AccentColor
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType

interface ChatService {

    val accentColors: StateFlow<Map<Int, AccentColor>>

    fun updateAccentColors(updFun: (Map<Int, AccentColor>) -> Map<Int, AccentColor>)

    val chats: StateFlow<Map<Long, Chat>>

    fun updateChats(updFun: (Map<Long, Chat>) -> Map<Long, Chat>)

    fun openChat(
        chatId: Long,
        onOK: () -> Unit
    )

    fun closeChat(chatId: Long)

    fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (ResponseData.Error) -> Unit
    )

    fun searchChatsLocal(
        query: String,
        limit: Int,
        onChats: (Map<Long,Chat>) -> Unit
    )
}