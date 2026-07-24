package org.gaziz.birgram.features.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.features.chat.domain.model.ChatInfo
import org.gaziz.birgram.features.chat.domain.usecase.GetChatById
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatById: GetChatById
): ViewModel() {
    val chat: (Long) -> StateFlow<ChatInfo?> = {
        getChatById(it).stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
    }
}