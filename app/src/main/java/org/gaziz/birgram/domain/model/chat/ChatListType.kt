package org.gaziz.birgram.domain.model.chat

sealed class ChatListType {
    object Main: ChatListType()
    object Archive: ChatListType()
    data class Folder(val id: Int): ChatListType()
}