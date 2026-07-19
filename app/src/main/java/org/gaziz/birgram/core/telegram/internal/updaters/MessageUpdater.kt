package org.gaziz.birgram.core.telegram.internal.updaters

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.internal.mapper.toMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageUpdater @Inject constructor(
    private val messageService: MessageService
) {
    fun onNewUpdate(u: TdApi.UpdateNewMessage) {
        if(!u.message.isOutgoing) {
            messageService.updateMessages { old ->
                old + (u.message.id to u.message.toMessage())
            }
        }
    }

    fun onSendSucceedUpdate(u: TdApi.UpdateMessageSendSucceeded){
        messageService.updateMessages { old ->
            old + (u.message.id to u.message.toMessage())
        }
    }

    fun onLoggingOut() {
        messageService.updateMessages { emptyMap() }
    }
}