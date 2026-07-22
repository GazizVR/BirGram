package org.gaziz.birgram.core.telegram.internal.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import org.gaziz.birgram.core.telegram.api.model.message.MessageSender

fun TdApi.MessageContent.toMessageCnt(): MessageContent {
    return when(val cnt = this) {
        is TdApi.MessageText -> MessageContent.Text(cnt.text.text)

        is TdApi.MessageSticker -> MessageContent.Sticker(cnt.sticker.emoji)

        is TdApi.MessageAnimation -> MessageContent.GIF(
            miniThumbnail = cnt.animation.minithumbnail?.data,
            caption = cnt.caption.text
        )

        is TdApi.MessagePhoto -> MessageContent.Photo(
            miniThumbnail = cnt.photo.minithumbnail?.data,
            caption = cnt.caption.text
        )

        is TdApi.MessageVideo -> MessageContent.Video(
            miniThumbnail = cnt.video.minithumbnail?.data,
            caption = cnt.caption.text
        )

        is TdApi.MessageAudio -> MessageContent.Audio(
            caption = cnt.caption.text
        )

        else -> MessageContent.Other
    }
}

fun TdApi.MessageSender.toSender(): MessageSender {
    return when(val sender = this) {
        is TdApi.MessageSenderUser -> MessageSender.User(sender.userId)
        is TdApi.MessageSenderChat -> MessageSender.Chat(sender.chatId)
        else -> MessageSender.Other
    }
}

fun TdApi.Message.toMessage(): Message {
    return Message(
        id = this.id,
        content = this.content.toMessageCnt(),
        date = this.date.fromUnixTimeStamp(),
        isMy = this.isOutgoing,
        chatId = this.chatId,
        sender = this.senderId.toSender()
    )
}