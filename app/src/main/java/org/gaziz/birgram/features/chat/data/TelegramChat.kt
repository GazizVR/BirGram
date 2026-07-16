package org.gaziz.birgram.features.chat.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.ClientManager
import org.gaziz.birgram.features.chat.data.mapper.toMessageData
import org.gaziz.birgram.features.chat.domain.model.ChatData
import org.gaziz.birgram.features.chat.domain.model.MessageData
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class TelegramChat @Inject constructor(
    private val manager: ClientManager
): ChatRepository {

    private val _chats = MutableStateFlow(emptyMap<Long, ChatData>())
    override val chats: StateFlow<Map<Long, ChatData>> = _chats.asStateFlow()

    private val _messages = MutableStateFlow(emptyMap<Long, MessageData>())
    override val messages: StateFlow<Map<Long, MessageData>> = _messages.asStateFlow()

    override fun setMessages(
        updFun: (Map<Long, MessageData>) -> Map<Long, MessageData>
    ) {
        _messages.update(updFun)
    }

    override fun openChat(
        chatId: Long,
        onOK: () -> Unit
    ) {
        manager.sendRequest(
            TdApi.OpenChat().apply { this.chatId = chatId },
        ) {
            if(it is TdApi.Ok) { onOK() }
        }
    }

    override fun closeChat(chatId: Long) {
        manager.sendRequest(
            TdApi.CloseChat().apply { this.chatId = chatId },
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