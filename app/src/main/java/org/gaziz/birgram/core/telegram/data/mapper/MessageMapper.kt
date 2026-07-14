package org.gaziz.birgram.core.telegram.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.domain.model.chat.LastMessageData
import org.gaziz.birgram.core.telegram.domain.model.message.MessageContent
import org.gaziz.birgram.core.telegram.domain.model.message.MessageData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun TdApi.MessageContent.toMessageCnt(): MessageContent {
    return when(val cnt = this) {
        is TdApi.MessageText -> MessageContent.Text(cnt.text.text)
        is TdApi.MessagePhoto -> MessageContent.Photo(cnt.caption.text,cnt.photo.minithumbnail?.data)
        is TdApi.MessageDocument -> MessageContent.Document(cnt.caption.text,cnt.document.fileName)
        is TdApi.MessageAudio -> MessageContent.Audio(cnt.caption.text,cnt.audio.fileName)
        is TdApi.MessageVideo -> MessageContent.Video(cnt.caption.text,cnt.video.fileName)

        is TdApi.MessageSticker -> MessageContent.Sticker(cnt.sticker.emoji)
        is TdApi.MessageVoiceNote -> MessageContent.VoiceNote
        is TdApi.MessageVideoNote -> MessageContent.VideoNote
        is TdApi.MessageAnimation -> MessageContent.GIF

        else -> MessageContent.Other
    }
}

fun Int.fromUnixTimeStamp(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return Instant
        .ofEpochSecond(this.toLong())
        .atZone(zoneId)
        .toLocalDateTime()
}

fun TdApi.Message?.toLastMsgData(): LastMessageData? {
    return if(this != null) {
        LastMessageData(
            id = this.id,
            content = this.content.toMessageCnt(),
            date = this.date.fromUnixTimeStamp()
        )
    } else {
        null
    }
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