package org.gaziz.birgram.features.chatList.domain.repository

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.domain.model.RequestResponse
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatData
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatListType

interface ChatListRepository {
    val chatList: StateFlow<Map<Long, ChatData>>

    suspend fun collectUpdates()
    fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (RequestResponse.Error) -> Unit
    )
    fun downloadChatPhoto(fileId: Int)
    fun logOut(onOk: () -> Unit)
}