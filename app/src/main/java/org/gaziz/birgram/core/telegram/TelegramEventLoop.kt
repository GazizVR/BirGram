package org.gaziz.birgram.core.telegram

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.data.mapper.toMessageData
import org.gaziz.birgram.core.telegram.domain.model.message.MessageData
import javax.inject.Inject

class TelegramEventLoop @Inject constructor(
    private val manager: TelegramManager
) {
    private val _messages = MutableStateFlow(emptyMap<Long, MessageData>())
    override val messages: StateFlow<Map<Long, MessageData>> = _messages.asStateFlow()

    override fun setMessages(
        updFun: (Map<Long, MessageData>) -> Map<Long, MessageData>
    ) {
        _messages.update(updFun)
    }

    override fun createEventLoop() {
        manager.createClient(
            { event ->
                when (event) {

                    is TdApi.UpdateMessageSendSucceeded -> {
                        _messages.update { map ->
                            val newMap = map.toMutableMap()
                            newMap[event.message.id] = event.message.toMessageData()
                            newMap.toMap()
                        }
                    }

                    is TdApi.UpdateNewMessage -> {
                        if(!event.message.isOutgoing) {
                            _messages.update { map ->
                                val newMap = map.toMutableMap()
                                newMap[event.message.id] = event.message.toMessageData()
                                newMap.toMap()
                            }
                        }
                    }
                }
            },
        )
    }

}