package org.gaziz.birgram.features.chat.data

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.mapper.toTgDraftMessage
import org.gaziz.birgram.core.telegram.internal.updaters.MessageUpdater
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepoImpl @Inject constructor(
    private val manager: ClientManager,
    private val tgMessage: MessageUpdater
): ChatRepository {

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

    override fun loadMessages(
        chatId: Long,
        fromMessageId: Long,
        onErr: () -> Unit
    ) {
        tgMessage.getChatMessages(chatId,fromMessageId) { onErr() }
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
               onMessage(it.toMessageData())
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