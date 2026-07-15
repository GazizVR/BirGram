package org.gaziz.birgram.core.telegram.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.model.ChatPhoto
import org.gaziz.birgram.core.telegram.model.ChatType
import org.gaziz.birgram.core.telegram.model.UserStatus

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
        is TdApi.ChatTypeSupergroup -> ChatType.SuperGroup(this.supergroupId, this.isChannel)
        else -> ChatType.Other
    }
}
