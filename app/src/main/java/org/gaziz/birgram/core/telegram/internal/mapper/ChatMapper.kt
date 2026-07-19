package org.gaziz.birgram.core.telegram.internal.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPermissions
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPosition
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType

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
        is TdApi.ChatTypePrivate -> ChatType.Private(this.userId)
        is TdApi.ChatTypeSupergroup -> ChatType.SuperGroup(this.supergroupId, this.isChannel)
        is TdApi.ChatTypeSecret -> ChatType.Secret(this.secretChatId,this.userId)
        else -> ChatType.Other
    }
}

fun TdApi.ChatPermissions.toPermissions(): ChatPermissions {
    return ChatPermissions(
        canSendBasicMessages = canSendBasicMessages
    )
}

fun TdApi.Chat.toChat(): Chat {
    val chat = this
    val chatPositions = mutableListOf<ChatPosition>()
        .apply { chat.positions.forEach { add(it.toChatPosition()) } }
        .toList()
    return Chat(
        id = chat.id,
        title = chat.title,
        type = chat.type.toType(),
        photo = chat.photo.toPhotoInfo(),
        lastMessage = chat.lastMessage?.toMessage(),
        draftMessage = chat.draftMessage.toDraftMessage(),
        positions = chatPositions,
        unreadCount = chat.unreadCount,
        mentionCount = chat.unreadMentionCount,
        reactionCount = chat.unreadReactionCount,
        permissions = chat.permissions.toPermissions(),
    )
}