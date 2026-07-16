package org.gaziz.birgram.features.searchChats.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.data.mapper.toPhotoInfo
import org.gaziz.birgram.core.telegram.data.mapper.toType
import org.gaziz.birgram.features.searchChats.domain.model.ChatData

fun TdApi.Chat.toChatData(): ChatData {
    val chat = this
    return ChatData(
        id = chat.id,
        title = chat.title,
        type = chat.type.toType(),
        photo = chat.photo.toPhotoInfo(),
    )
}
