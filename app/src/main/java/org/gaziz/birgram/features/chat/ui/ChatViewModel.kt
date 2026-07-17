package org.gaziz.birgram.features.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.model.DraftMessage
import org.gaziz.birgram.core.telegram.model.DraftMessageContent
import org.gaziz.birgram.features.chat.domain.model.ChatData
import org.gaziz.birgram.features.chat.domain.model.MessageData
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
import org.gaziz.birgram.features.chat.domain.usecase.GetChatByID
import org.gaziz.birgram.features.chat.domain.usecase.GetChatMessages
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatByID: GetChatByID,
    private val getChatMessages: GetChatMessages,
    private val chatRepository: ChatRepository
): ViewModel() {
    private var isMessagesLoading = false
    fun openChat(
        chatId: Long,
    ){
        chatRepository.openChat(
            chatId
        ) {
            chatRepository.loadMessages(chatId, onErr = { isMessagesLoading = false })
        }
    }

    fun closeChat(chatId: Long){ chatRepository.closeChat(chatId) }

    fun loadMessages(
        chatId: Long,
        lastMessageId: Long
    )  {
        if(isMessagesLoading) return
        chatRepository.loadMessages(chatId,lastMessageId) { isMessagesLoading = false }
    }

    fun getChat(chatId: Long): StateFlow<ChatData?> {
       return getChatByID(chatId).stateIn(
           viewModelScope,
           SharingStarted.Eagerly,
           null
       )
    }
    fun chatMessages(chatId: Long): StateFlow<Map<LocalDate, List<MessageData>>> {
        return getChatMessages(chatId).stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyMap()
        )
    }

    fun sendMessage(
        chatId: Long,
        message: String
    ) {
        viewModelScope.launch {
            chatRepository.sendMessage(chatId,message,{})
        }
    }

    fun setDraftMessage(
        chatId: Long,
        text: String
    ) {
        viewModelScope.launch {
            chatRepository.setDraftMessage(
                chatId,
                DraftMessage(
                    content = DraftMessageContent.Text(text,false),
                    date = LocalDateTime.now()
                )
            )
        }
    }
}