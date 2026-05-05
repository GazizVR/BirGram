package org.gaziz.birgram.presentation.chatList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.repository.EventLoopRepository
import org.gaziz.birgram.domain.usecase.GetChatList
import org.gaziz.birgram.domain.usecase.LoadAllChats
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val eventLoopRepository: EventLoopRepository,
    private val loadAllChats: LoadAllChats,
    private val getChatList: GetChatList
): ViewModel() {
    init {
        loadAllChats(ChatListType.Main)
    }
    val chatList = getChatList(ChatListType.Main).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )
}