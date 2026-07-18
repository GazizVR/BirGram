package org.gaziz.birgram.features.chatList.domain.repository

import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import org.gaziz.birgram.core.telegram.api.model.ResponseData

interface ChatListRepository {
    fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (ResponseData.Error) -> Unit
    )
    fun downloadChatIcon(
        chatId: Long,
        fileId: Int
    )
    fun logOut(onOk: () -> Unit)
}