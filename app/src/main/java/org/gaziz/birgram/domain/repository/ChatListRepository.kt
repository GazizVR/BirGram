package org.gaziz.birgram.domain.repository

import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.model.chatList.RequestResponse

interface ChatListRepository {
    fun loadChats(
        limit: Int,
        listType: ChatListType
    ): RequestResponse
}