package org.gaziz.birgram.presentation.chatList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.repository.ChatListRepository
import org.gaziz.birgram.domain.usecase.GetChatList
import org.gaziz.birgram.domain.usecase.LoadAllChats
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    loadAllChats: LoadAllChats,
    getChatList: GetChatList,
    private val chatListRepository: ChatListRepository
): ViewModel() {
    init {
        loadAllChats(ChatListType.Main)
    }
    val mainChatList = getChatList(ChatListType.Main).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )
    fun downloadChatPhoto(id: Int) {
       viewModelScope.launch {
           chatListRepository.downloadChatPhoto(id)
       }
    }
}