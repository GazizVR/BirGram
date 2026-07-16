package org.gaziz.birgram.core.telegram.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramMessage @Inject constructor() {
    private val _messages = MutableStateFlow<Map<Long, TdApi.Message>>(emptyMap())
    val message = _messages.asStateFlow()

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
}