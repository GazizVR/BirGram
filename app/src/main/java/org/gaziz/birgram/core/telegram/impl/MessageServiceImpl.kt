package org.gaziz.birgram.core.telegram.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.mapper.toMessage
import org.gaziz.birgram.core.telegram.internal.mapper.toTgDraftMessage
import javax.inject.Inject

class MessageServiceImpl @Inject constructor(
    private val manager: ClientManager
): MessageService {
    private val _messages = MutableStateFlow<Map<Long, Message>>(emptyMap())
    override val messages = _messages.asStateFlow()

    override fun updateMessages(updFun: (Map<Long, Message>) -> Map<Long, Message>) {
        _messages.update(updFun)
    }

    override fun getMessages(
        chatId: Long,
        fromMessage: Long,
        onError: () -> Unit
    ) {
        manager.sendRequest(
            TdApi.GetChatHistory().apply {
                this.chatId = chatId
                this.fromMessageId = fromMessage
                this.offset = 0
                this.limit = 50
                this.onlyLocal = false
            },
            {onError()}
        ) { resp ->
            if(resp is TdApi.Messages) {
                _messages.update { map ->
                    map.toMutableMap().apply {
                        resp.messages.forEach { msg -> put(msg.id,msg.toMessage()) }
                    }.toMap()
                }
            }
        }
    }

    override fun sendMessage(
        chatId: Long,
        content: String,
        onMessage: (Message?) -> Unit
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
                onMessage(it.toMessage())
            } else {
                onMessage(null)
            }
        }
    }

    override fun setDraftMessage(
        chatId: Long,
        draftMessage: DraftMessage
    ) {
        manager.sendRequest(
            TdApi.SetChatDraftMessage().apply {
                this.chatId = chatId
                this.topicId = null
                this.draftMessage = draftMessage.toTgDraftMessage()
            }
        )
    }
}