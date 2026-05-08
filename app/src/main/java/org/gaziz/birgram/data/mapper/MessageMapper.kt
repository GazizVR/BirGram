package org.gaziz.birgram.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.chat.LastMessageContent
import org.gaziz.birgram.domain.model.chat.LastMessageData
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.formatForChatList(locale: Locale = Locale.getDefault()): String {
    val now = LocalDate.now()
    val date = toLocalDate()

    return when {
        date == now -> format(DateTimeFormatter.ofPattern("HH:mm"))

        date.isAfter(now.minusDays(7)) -> format(DateTimeFormatter.ofPattern("EEE", locale))

        date.year == now.year -> format(DateTimeFormatter.ofPattern("d MMM", locale))

        else -> format(DateTimeFormatter.ofPattern("dd.MM.yy"))
    }
}

fun TdApi.Message?.toMessageData(): LastMessageData? {
    return if(this != null) {
        val lastMessageContent = when(val cnt = this.content) {
            is TdApi.MessageText -> LastMessageContent.Text(cnt.text.text)
            is TdApi.MessagePhoto -> LastMessageContent.Photo(cnt.caption.text,cnt.photo.minithumbnail?.data)
            is TdApi.MessageDocument -> LastMessageContent.Document(cnt.caption.text,cnt.document.fileName)
            is TdApi.MessageAudio -> LastMessageContent.Audio(cnt.caption.text,cnt.audio.fileName)
            is TdApi.MessageVideo -> LastMessageContent.Video(cnt.caption.text,cnt.video.fileName)

            is TdApi.MessageSticker -> LastMessageContent.Sticker(cnt.sticker.emoji)
            is TdApi.MessageVoiceNote -> LastMessageContent.VoiceNote
            is TdApi.MessageVideoNote -> LastMessageContent.VideoNote
            is TdApi.MessageAnimation -> LastMessageContent.GIF

            else -> LastMessageContent.Other(this.content.toString())
        }
        LastMessageData(
            id = this.id,
            content = lastMessageContent,
            date = Instant.ofEpochSecond(this.date.toLong()).atZone(ZoneId.systemDefault()).toLocalDateTime().formatForChatList()
        )
    } else {
        null
    }
}