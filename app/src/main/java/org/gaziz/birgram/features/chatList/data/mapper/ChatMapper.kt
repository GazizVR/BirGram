package org.gaziz.birgram.features.chatList.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.mapper.toPhotoInfo
import org.gaziz.birgram.core.telegram.mapper.toType
import org.gaziz.birgram.features.chatList.domain.model.ChatData
import org.gaziz.birgram.features.chatList.domain.model.ChatListType
import org.gaziz.birgram.features.chatList.domain.model.ChatPosition

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

fun TdApi.Chat.toChatData(): ChatData {
    val chat = this
    val chatPositions = mutableListOf<ChatPosition>()
        .apply { chat.positions.forEach { add(it.toChatPosition()) } }
        .toList()
    return ChatData(
        id = chat.id,
        title = chat.title,
        type = chat.type.toType(),
        photo = chat.photo.toPhotoInfo(),
        lastMessage = chat.lastMessage.toLastMsgData(),
        positions = chatPositions,
        unreadCount = chat.unreadCount,
        mentionCount = chat.unreadMentionCount,
        reactionCount = chat.unreadReactionCount
    )
}