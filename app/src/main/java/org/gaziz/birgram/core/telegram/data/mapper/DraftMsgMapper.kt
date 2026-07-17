package org.gaziz.birgram.core.telegram.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.model.DraftMessage
import org.gaziz.birgram.core.telegram.model.DraftMessageContent
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

fun TdApi.InputMessageContent.toDraftMsgCnt(): DraftMessageContent {
    return when(this) {
        is TdApi.InputMessageText -> DraftMessageContent.Text(
            text = this.text.text,
            clearDraft = this.clearDraft
        )
        else -> DraftMessageContent.Other
    }
}

fun TdApi.DraftMessage?.toDraftMessage(): DraftMessage? {
    if(this == null) return null
    return DraftMessage(
        content = this.inputMessageText.toDraftMsgCnt(),
        date = this.date.fromUnixTimeStamp()
    )
}

fun DraftMessageContent.toTgDraftMsgCnt(): TdApi.InputMessageContent {
    return when(val msg = this) {
        is DraftMessageContent.Text -> {
            TdApi.InputMessageText().apply {
                text = TdApi.FormattedText().apply {
                    text = msg.text
                }
            }
        }
        DraftMessageContent.Other -> {
            TdApi.InputMessageText().apply {
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
        inputMessageText = msg.content.toTgDraftMsgCnt()
    }
}