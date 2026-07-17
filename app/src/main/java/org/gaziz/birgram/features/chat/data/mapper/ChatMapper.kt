package org.gaziz.birgram.features.chat.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.data.mapper.toDraftMessage
import org.gaziz.birgram.core.telegram.data.mapper.toPhotoInfo
import org.gaziz.birgram.core.telegram.data.mapper.toType
import org.gaziz.birgram.features.chat.domain.model.ChatData

fun TdApi.Chat.toChatData(): ChatData {
    val chat = this
    return ChatData(
        id = chat.id,
        title = chat.title,
        type = chat.type.toType(),
        photo = chat.photo.toPhotoInfo(),
        canSendBasicMsg = chat.permissions.canSendBasicMessages,
        draftMessage = chat.draftMessage.toDraftMessage()
    )
}