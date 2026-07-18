package org.gaziz.birgram.core.telegram.chats.api.model

sealed class ChatListType {
    object Main: ChatListType()
    object Archive: ChatListType()
    data class Folder(val id: Int): ChatListType()
}