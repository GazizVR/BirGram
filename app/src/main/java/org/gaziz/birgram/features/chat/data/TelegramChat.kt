package org.gaziz.birgram.features.chat.data

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.core.telegram.data.mapper.toMessageData
import org.gaziz.birgram.core.telegram.domain.model.message.MessageData
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class TelegramChat @Inject constructor(
    private val manager: TelegramManager
): ChatRepository {
    override fun openChat(
        chatId: Long,
        onOK: () -> Unit
    ) {
        manager.sendRequest(
            TdApi.OpenChat().apply { this.chatId = chatId },
            {}
        ) {
            if(it is TdApi.Ok) { onOK() }
        }
    }

    override fun closeChat(chatId: Long) {
        manager.sendRequest(
            TdApi.CloseChat().apply { this.chatId = chatId },
            {}
        )
    }

    override fun getChatMessages(
        chatId: Long,
        fromMessage: Long,
        onMessages: (List<MessageData>) -> Unit
    ) {
        manager.sendRequest(
            TdApi.GetChatHistory().apply {
                this.chatId = chatId
                this.fromMessageId = fromMessage
                this.offset = 0
                this.limit = 50
                this.onlyLocal = false
            },
            { onMessages(emptyList()) }
        ) { resp ->
            if(resp is TdApi.Messages) {
                val msgs = mutableListOf<MessageData>()
                resp.messages.forEach { msgs.add(it.toMessageData()) }
                onMessages(msgs.toList())
            } else {
                onMessages(emptyList())
            }
        }
    }

    override fun sendMessage(
        chatId: Long,
        content: String,
        onMessage: (MessageData?) -> Unit
    ) {
       manager.sendRequest(
           TdApi.SendMessage().apply {
               this.chatId = chatId
               this.topicId = null
               this.replyTo = null
               this.options = null
               this.replyMarkup = null
               this.inputMessageContent = TdApi.InputMessageText().apply {
                   this.text = TdApi.FormattedText().apply { this.text = content }
                   this.linkPreviewOptions = null
                   this.clearDraft = true
               }
           },
           { onMessage(null) }
       ){
           if(it is TdApi.Message){
               onMessage(it.toMessageData())
           } else {
               onMessage(null)
           }
       }
    }
}