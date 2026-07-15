package org.gaziz.birgram.features.chatList.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.features.chatList.domain.model.chat.ChatData
import org.gaziz.birgram.features.chatList.domain.model.chat.ChatListType
import org.gaziz.birgram.features.chatList.domain.model.RequestResponse

interface ChatListRepository {
    val chatList: StateFlow<Map<Long, ChatData>>

    fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (RequestResponse.Error) -> Unit
    )
    fun downloadChatPhoto(fileId: Int)
    fun logOut(onOk: () -> Unit)
}