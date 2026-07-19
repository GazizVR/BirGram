package org.gaziz.birgram.features.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.MessageService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessage
import org.gaziz.birgram.core.telegram.api.model.message.DraftMessageContent
import org.gaziz.birgram.core.telegram.api.model.message.Message
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.features.chat.domain.usecase.GetChatMessages
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val userService: UserService,
    private val getChatMessages: GetChatMessages,
    private val messageService: MessageService,
    private val chatService: ChatService
): ViewModel() {
    fun getUser(userId: Long): StateFlow<User?> {
        return userService.users.map {
           it[userId]
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
    }
    private var isMessagesLoading = false
    fun openChat(
        chatId: Long,
    ){
        chatService.openChat(
            chatId
        ) {
            messageService.loadMessages(chatId, onError = { isMessagesLoading = false })
        }
    }

    fun closeChat(chatId: Long){ chatService.closeChat(chatId) }

    fun loadMessages(
        chatId: Long,
        lastMessageId: Long
    )  {
        if(isMessagesLoading) return
        messageService.loadMessages(chatId,lastMessageId) { isMessagesLoading = false }
    }

    fun getChat(chatId: Long): StateFlow<Chat?> {
       return chatService.chats.map {
           it[chatId]
       }.stateIn(
           viewModelScope,
           SharingStarted.Eagerly,
           null
       )
    }
    fun chatMessages(chatId: Long): StateFlow<Map<LocalDate, List<Message>>> {
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
            messageService.sendMessage(chatId,message,{})
        }
    }

    fun setDraftMessage(
        chatId: Long,
        text: String
    ) {
        viewModelScope.launch {
            messageService.setDraftMessage(
                chatId,
                DraftMessage(
                    content = DraftMessageContent.Text(text,false),
                    date = LocalDateTime.now()
                )
            )
        }
    }
}