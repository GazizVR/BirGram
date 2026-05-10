package org.gaziz.birgram.presentation.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.domain.model.chat.ChatData
import org.gaziz.birgram.domain.model.message.MessageData
import org.gaziz.birgram.domain.repository.ChatRepository
import org.gaziz.birgram.domain.usecase.chat.GetChatByID
import org.gaziz.birgram.domain.usecase.chat.GetChatMessages
import org.gaziz.birgram.domain.usecase.chat.LoadChatMessages
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatByID: GetChatByID,
    private val getChatMessages: GetChatMessages,
    private val loadChatMessages: LoadChatMessages,
    private val chatRepository: ChatRepository
): ViewModel() {

    fun openChat(
        chatId: Long
    ){
        chatRepository.openChat(
            chatId
        ) {
            loadChatMessages(chatId)
        }
    }

    fun closeChat(chatId: Long){ chatRepository.closeChat(chatId) }

    fun loadMessages(
        chatId: Long,
        lastMessageId: Long
    )  {
        loadChatMessages(chatId,lastMessageId)
    }

    fun getChat(chatId: Long): StateFlow<ChatData?> {
       return getChatByID(chatId).stateIn(
           viewModelScope,
           SharingStarted.Eagerly,
           null
       )
    }
    fun chatMessages(chatId: Long): StateFlow<List<MessageData>> {
        return getChatMessages(chatId).stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )
    }
}