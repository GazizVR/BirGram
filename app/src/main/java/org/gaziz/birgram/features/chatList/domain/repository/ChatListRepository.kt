package org.gaziz.birgram.features.chatList.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.domain.model.RequestResponse
import org.gaziz.birgram.features.chatList.domain.model.ChatData
import org.gaziz.birgram.features.chatList.domain.model.ChatListType

interface ChatListRepository {
    val chats: StateFlow<Map<Long, ChatData>>

    fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (RequestResponse.Error) -> Unit
    )
    fun downloadChatPhoto(fileId: Int)
    fun logOut(onOk: () -> Unit)
}