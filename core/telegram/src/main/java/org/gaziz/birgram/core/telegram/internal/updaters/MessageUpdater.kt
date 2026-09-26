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
        messageService.updateMessages { old ->
            old + (u.message.id to u.message.toMessage())
        }
    }

    fun onSendSucceedUpdate(u: TdApi.UpdateMessageSendSucceeded){
        messageService.updateMessages { old ->
            val new = old - u.oldMessageId
            new + (u.message.id to u.message.toMessage())
        }
    }

    fun onSendFailedUpdate(u: TdApi.UpdateMessageSendFailed) {
        messageService.updateMessages { old ->
            old + (u.oldMessageId to u.message.toMessage())
        }
    }

    fun onDeleteMessagesUpdate(u: TdApi.UpdateDeleteMessages) {
       messageService.updateMessages { old ->
           old
               .toMutableMap()
               .apply {
                   for(msgId in u.messageIds) {
                       val chatId = get(msgId)?.chatId ?: continue
                       if(chatId == u.chatId) remove(msgId)
                   }
               }
               .toMap()
       }
    }

    fun onLoggingOut() {
        messageService.updateMessages { emptyMap() }
    }
}