package org.gaziz.birgram.features.chatList.domain.repository

import org.gaziz.birgram.core.telegram.model.RequestResponse
import org.gaziz.birgram.features.chatList.domain.model.ChatListType

interface ChatListRepository {
    fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (RequestResponse.Error) -> Unit
    )
    fun downloadChatPhoto(fileId: Int)
    fun logOut(onOk: () -> Unit)
}