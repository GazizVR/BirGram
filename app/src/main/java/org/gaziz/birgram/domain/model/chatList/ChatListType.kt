package org.gaziz.birgram.domain.model.chatList

sealed class ChatListType {
    object Main : ChatListType()
    object Archive: ChatListType()
    data class Folder(val folderId: Int): ChatListType()
}