package org.gaziz.birgram.features.chatList.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.features.chat.data.mapper.fromUnixTimeStamp
import org.gaziz.birgram.features.chatList.domain.model.MessageContent
import org.gaziz.birgram.features.chatList.domain.model.MessageData

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

fun TdApi.Message?.toLastMsgData(): MessageData? {
    return if(this != null) {
        MessageData(
            id = this.id,
            content = this.content.toMessageCnt(),
            date = this.date.fromUnixTimeStamp()
        )
    } else {
        null
    }
}
