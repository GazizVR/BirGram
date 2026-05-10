package org.gaziz.birgram.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.UserStatus
import org.gaziz.birgram.domain.model.chat.ChatData
import org.gaziz.birgram.domain.model.chat.ChatListType
import org.gaziz.birgram.domain.model.chat.ChatPhoto
import org.gaziz.birgram.domain.model.chat.ChatPosition
import org.gaziz.birgram.domain.model.chat.ChatType

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

fun TdApi.ChatType.toType(): ChatType {
    return when(this) {
        is TdApi.ChatTypeBasicGroup -> ChatType.BasicGroup(this.basicGroupId)
        is TdApi.ChatTypePrivate -> ChatType.Private(this.userId, UserStatus.Recently)
        is TdApi.ChatTypeSupergroup -> ChatType.SuperGroup(this.supergroupId,this.isChannel)
        is TdApi.ChatTypeSecret -> ChatType.Secret(this.secretChatId,this.userId,UserStatus.Recently)
        else -> ChatType.Other
    }
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