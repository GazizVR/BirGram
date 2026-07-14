package org.gaziz.birgram.features.chatList.domain.repository

import org.gaziz.birgram.core.telegram.domain.model.RequestResponse
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatListType

interface ChatListRepository {
    fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (RequestResponse.Error) -> Unit
    )
    fun downloadChatPhoto(fileId: Int)
}