package org.gaziz.birgram.domain.repository

import org.gaziz.birgram.domain.model.RequestResponse
import org.gaziz.birgram.domain.model.chat.ChatListType

interface ChatListRepository {
    fun loadChats(
        limit: Int,
        listType: ChatListType
    ): RequestResponse
    fun downloadChatPhoto(fileId: Int)
}