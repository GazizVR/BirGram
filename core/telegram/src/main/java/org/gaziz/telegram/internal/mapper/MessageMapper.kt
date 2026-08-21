package org.gaziz.telegram.internal.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.telegram.api.model.message.DraftMessage
import org.gaziz.telegram.api.model.message.DraftMessageContent
import org.gaziz.telegram.api.model.message.Message
import org.gaziz.telegram.api.model.message.MessageContent
import org.gaziz.telegram.api.model.message.MessageSender
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun Int.fromUnixTimeStamp(zoneId: ZoneId = ZoneId.systemDefault()): LocalDateTime {
    return Instant
        .ofEpochSecond(this.toLong())
        .atZone(zoneId)
        .toLocalDateTime()
}

fun TdApi.DraftMessageContent.toDraftMsgCnt(): DraftMessageContent {
    return when(this) {
        is TdApi.DraftMessageContentText -> DraftMessageContent.Text(
            text = this.text.text
        )
        else -> DraftMessageContent.Other
    }
}

fun TdApi.DraftMessage?.toDraftMessage(): DraftMessage? {
    if(this == null) return null
    return DraftMessage(
        content = this.content.toDraftMsgCnt(),
        date = this.date.fromUnixTimeStamp()
    )
}

fun DraftMessageContent.toTgDraftMsgCnt(): TdApi.DraftMessageContent {
    return when(val msg = this) {
        is DraftMessageContent.Text -> {
            TdApi.DraftMessageContentText().apply {
                text = TdApi.FormattedText().apply {
                    text = msg.text
                }
            }
        }
        DraftMessageContent.Other -> {
            TdApi.DraftMessageContentText().apply {
                text = TdApi.FormattedText().apply {
                    text = ""
                }
            }
        }
    }
}

fun DraftMessage.toTgDraftMessage(): TdApi.DraftMessage {
    val msg = this
    return TdApi.DraftMessage().apply {
        date = msg.date.toEpochSecond(ZoneOffset.UTC).toInt()
        content = msg.content.toTgDraftMsgCnt()
    }
}

fun TdApi.MessageContent.toMessageCnt(): MessageContent {
    return when(val cnt = this) {
        is TdApi.MessageText -> MessageContent.Text(cnt.text.text)

        is TdApi.MessageSticker -> MessageContent.Sticker(
            emoji = cnt.sticker.emoji,
            format = cnt.sticker.format.toFormat(),
            data = cnt.sticker.sticker.toFileData()
        )

        is TdApi.MessageAnimatedEmoji -> {
            var emojiSticker: MessageContent.Sticker? = null
            val sticker = cnt.animatedEmoji.sticker
            if(sticker != null) {
                emojiSticker = MessageContent.Sticker(
                    emoji = sticker.emoji,
                    format = sticker.format.toFormat(),
                    data = sticker.sticker.toFileData()
                )
            }
            MessageContent.AnimatedEmoji(
                emoji = cnt.emoji,
                animation = emojiSticker
            )
        }

        is TdApi.MessageAnimation -> MessageContent.Animation(
            miniThumbnail = cnt.animation.minithumbnail?.data,
            caption = cnt.caption.text,
            file = cnt.animation.animation.toFileData(),
            width = cnt.animation.width,
            height = cnt.animation.height,
            mimeType = cnt.animation.mimeType,
        )

        is TdApi.MessagePhoto -> {
            val sizes = cnt.photo.sizes.map {
                it.toSize()
            }
            MessageContent.Photo(
                miniThumbnail = cnt.photo.minithumbnail?.data,
                caption = cnt.caption.text,
                sizes = sizes
            )
        }

        is TdApi.MessageVideo -> MessageContent.Video(
            miniThumbnail = cnt.video.minithumbnail?.data,
            caption = cnt.caption.text,
            file = cnt.video.video.toFileData(),
            width = cnt.video.width,
            height = cnt.video.height
        )

        is TdApi.MessageAudio -> MessageContent.Audio(
            caption = cnt.caption.text
        )

        is TdApi.MessageDocument -> MessageContent.Document(
            fileName = cnt.document.fileName,
            caption = cnt.caption.text,
            file = cnt.document.document.toFileData(),
            mimeType = cnt.document.mimeType
        )

        is TdApi.MessageVoiceNote -> MessageContent.VoiceNote

        is TdApi.MessageVideoNote -> MessageContent.VideoNote(
            miniThumbnail = cnt.videoNote.minithumbnail?.data
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
        isOutgoing = this.isOutgoing,
        chatId = this.chatId,
        sender = this.senderId.toSender()
    )
}