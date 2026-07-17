package org.gaziz.birgram.features.chatList.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.features.chat.data.mapper.fromUnixTimeStamp
import org.gaziz.birgram.features.chatList.domain.model.LastMsgContent
import org.gaziz.birgram.features.chatList.domain.model.LastMsgData

fun TdApi.MessageContent.toMessageCnt(): LastMsgContent {
    return when(val cnt = this) {
        is TdApi.MessageText -> LastMsgContent.Text(cnt.text.text)
        is TdApi.MessagePhoto -> LastMsgContent.Photo(cnt.caption.text,cnt.photo.minithumbnail?.data)
        is TdApi.MessageDocument -> LastMsgContent.Document(cnt.caption.text,cnt.document.fileName)
        is TdApi.MessageAudio -> LastMsgContent.Audio(cnt.caption.text,cnt.audio.fileName)
        is TdApi.MessageVideo -> LastMsgContent.Video(cnt.caption.text,cnt.video.fileName)

        is TdApi.MessageSticker -> LastMsgContent.Sticker(cnt.sticker.emoji)
        is TdApi.MessageVoiceNote -> LastMsgContent.VoiceNote
        is TdApi.MessageVideoNote -> LastMsgContent.VideoNote
        is TdApi.MessageAnimation -> LastMsgContent.GIF

        else -> LastMsgContent.Other
    }
}

fun TdApi.Message?.toLastMsgData(): LastMsgData? {
    return if(this != null) {
        LastMsgData(
            id = this.id,
            content = this.content.toMessageCnt(),
            date = this.date.fromUnixTimeStamp()
        )
    } else {
        null
    }
}
