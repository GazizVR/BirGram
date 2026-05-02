package org.gaziz.birgram.domain.repository

import org.gaziz.birgram.domain.model.chatList.ChatListType

interface ChatListRepository {
    fun loadChats(
        type: ChatListType,
        limit: Int,
        onError: (String) -> Unit
    )

    fun downloadFile(
        fileId: Int,
        priority: Int,
        offset: Long,
        limit: Long,
        synchronous: Boolean,
        onError: (String) -> Unit
    )
}