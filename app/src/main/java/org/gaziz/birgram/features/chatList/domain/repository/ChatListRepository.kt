package org.gaziz.birgram.features.chatList.domain.repository

import org.gaziz.birgram.core.telegram.model.ResponseData
import org.gaziz.birgram.features.chatList.domain.model.ChatListType

interface ChatListRepository {
    fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (ResponseData.Error) -> Unit
    )
    fun downloadChatPhoto(fileId: Int)
    fun logOut(onOk: () -> Unit)
}