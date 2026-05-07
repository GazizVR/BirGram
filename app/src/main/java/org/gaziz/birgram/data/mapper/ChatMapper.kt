package org.gaziz.birgram.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.model.chatList.ChatPhoto
import org.gaziz.birgram.domain.model.chatList.ChatPosition

fun TdApi.ChatList.toChatListType(): ChatListType {
    return when(this){
        is TdApi.ChatListArchive -> ChatListType.Archive
        is TdApi.ChatListFolder -> ChatListType.Folder(this.chatFolderId)
        else -> ChatListType.Main
    }
}

fun TdApi.ChatPosition.toChatPosition(): ChatPosition {
    return ChatPosition(
        listType = this.list.toChatListType(),
        order = this.order,
        isPinned = this.isPinned
    )
}

fun TdApi.ChatPhotoInfo?.toPhotoInfo(): ChatPhoto? {
    return if(this != null) {
        ChatPhoto(
            miniThumbnail = this.minithumbnail?.data,
            small = this.small.toFileData()
        )
    } else {
        null
    }
}