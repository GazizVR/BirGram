package org.gaziz.birgram.features.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.features.chat.domain.model.ChatInfo
import org.gaziz.birgram.features.chat.domain.usecase.GetChatById
import org.gaziz.birgram.features.chat.domain.usecase.GetChatMessages
import org.gaziz.birgram.features.chat.domain.usecase.LoadChatMessages
import org.gaziz.birgram.features.chat.ui.mapper.formatMonthDay
import org.gaziz.birgram.features.chat.ui.model.MessageUiState
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatById: GetChatById,
    private val getChatMessages: GetChatMessages,
    private val chatService: ChatService,
    private val loadChatMessages: LoadChatMessages
): ViewModel() {
    fun openChat(
        chatId: Long,
    ) {
        chatService.openChat(chatId) {
            loadChatMessages(chatId)
        }
    }
    fun closeChat(
        chatId: Long,
    ) {
        chatService.closeChat(chatId)
    }
    val chat: (Long) -> StateFlow<ChatInfo?> = {
        getChatById(it).stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
    }
    val messages: (Long) -> StateFlow<Map<String, List<MessageUiState>>> = {
        getChatMessages(it).map { map ->
            map.entries.associate { (key,value) ->
                val messages = value.map { msg ->
                    MessageUiState(
                        id = msg.id,
                        content = msg.content,
                        isOutgoing = msg.isOutgoing,
                        date = msg.date.formatMonthDay()
                    )
                }
                key.formatMonthDay() to messages
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyMap()
        )
    }
    fun loadMessages(
        chatId: Long,
        fromMessageId: Long
    ){
        loadChatMessages(chatId,fromMessageId)
    }
}