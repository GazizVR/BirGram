package org.gaziz.birgram.core.telegram.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.domain.model.message.MessageData
import org.gaziz.birgram.features.chatList.domain.model.MessageContent
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


fun Int.fromUnixTimeStamp(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return Instant
        .ofEpochSecond(this.toLong())
        .atZone(zoneId)
        .toLocalDateTime()
}


fun TdApi.Message.toMessageData(): MessageData {
    return MessageData(
        id = this.id,
        content = this.content.toMessageCnt(),
        date = this.date.fromUnixTimeStamp(),
        isMy = this.isOutgoing,
        chatId = this.chatId
    )
}