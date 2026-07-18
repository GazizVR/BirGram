package org.gaziz.birgram.core.telegram.chats.impl

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.files.impl.toFileData
import org.gaziz.birgram.core.telegram.chats.api.model.ChatPhoto
import org.gaziz.birgram.core.telegram.chats.api.model.ChatType

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
        else -> ChatType.Other
    }
}
