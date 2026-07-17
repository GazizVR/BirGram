package org.gaziz.birgram.core.telegram.data.mapper

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.model.DraftMessage
import org.gaziz.birgram.core.telegram.model.DraftMessageContent
import org.gaziz.birgram.features.chat.data.mapper.fromUnixTimeStamp

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