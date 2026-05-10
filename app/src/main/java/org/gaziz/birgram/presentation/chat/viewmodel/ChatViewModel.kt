package org.gaziz.birgram.presentation.chat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.domain.model.chat.ChatData
import org.gaziz.birgram.domain.usecase.GetChatByID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatByID: GetChatByID
): ViewModel() {
    fun getChat(chatId: Long): StateFlow<ChatData?> {
       return getChatByID(chatId).stateIn(
           viewModelScope,
           SharingStarted.Eagerly,
           null
       )
    }
}