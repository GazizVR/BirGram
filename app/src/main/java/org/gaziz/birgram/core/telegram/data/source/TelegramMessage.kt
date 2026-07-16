package org.gaziz.birgram.core.telegram.data.source

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.ClientManager
import org.gaziz.birgram.core.telegram.model.ResponseData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramMessage @Inject constructor(
    private val manager: ClientManager
) {
    private val _messages = MutableStateFlow<Map<Long, TdApi.Message>>(emptyMap())
    val messages = _messages.asStateFlow()
    fun getChatMessages(
        chatId: Long,
        fromMessage: Long,
        onError: (ResponseData) -> Unit
    ) {
        manager.sendRequest(
            TdApi.GetChatHistory().apply {
                this.chatId = chatId
                this.fromMessageId = fromMessage
                this.offset = 0
                this.limit = 50
                this.onlyLocal = false
            },
            onError
        ) { resp ->
            if(resp is TdApi.Messages) {
                _messages.update { map ->
                   map.toMutableMap().apply {
                       resp.messages.forEach { msg -> put(msg.id,msg) }
                   }.toMap()
                }
            }
        }
    }

    fun onNewUpdate(u: TdApi.UpdateNewMessage) {
        if(!u.message.isOutgoing) {
            _messages.update { map ->
                map.toMutableMap().apply{ put(u.message.id,u.message) }.toMap()
            }
        }
    }

    fun onSendSucceedUpdate(u: TdApi.UpdateMessageSendSucceeded){
        _messages.update { map ->
            map.toMutableMap().apply{ put(u.message.id,u.message) }.toMap()
        }
    }

    fun onLoggingOut() {
        _messages.update { emptyMap() }
    }
}