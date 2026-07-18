package org.gaziz.birgram.core.telegram.api.model.chat

sealed interface ChatListType {
    object Main: ChatListType
    object Archive: ChatListType
    data class Folder(val id: Int): ChatListType
}