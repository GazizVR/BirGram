package org.gaziz.birgram.core.telegram.internal.updater

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.internal.mapper.toMessage
import org.gaziz.birgram.core.telegram.internal.mapper.toMessageCnt
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageUpdater @Inject constructor(
    private val messageService: MessageService,
) {
    fun onNewMessage(u: TdApi.UpdateNewMessage) {
        messageService.updateMessages { old ->
            old + (u.message.id to u.message.toMessage())
        }
    }

    fun onSendSucceeded(u: TdApi.UpdateMessageSendSucceeded){
        messageService.updateMessages { old ->
            val new = old - u.oldMessageId
            new + (u.message.id to u.message.toMessage())
        }
    }

    fun onSendFailed(u: TdApi.UpdateMessageSendFailed) {
        messageService.updateMessages { old ->
            old + (u.oldMessageId to u.message.toMessage())
        }
    }

    fun onDeleteMessages(u: TdApi.UpdateDeleteMessages) {
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

    fun onMessageContent(u: TdApi.UpdateMessageContent){
        messageService.updateMessages { old ->
            old
                .toMutableMap()
                .apply {
                    val message = get(u.messageId)
                    if(
                        message != null &&
                        message.chatId == u.chatId
                    ) {
                        put(
                            u.messageId,
                            message.copy(
                                content = u.newContent.toMessageCnt(),
                            )
                        )
                    }
                }
                .toMap()
        }
    }

    fun onLoggingOut() {
        messageService.updateMessages { emptyMap() }
    }
}