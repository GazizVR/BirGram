package org.gaziz.birgram.presentation.chatList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.domain.model.chat.ChatListType
import org.gaziz.birgram.domain.repository.ChatListRepository
import org.gaziz.birgram.core.telegram.EventLoopRepository
import org.gaziz.birgram.domain.repository.UserPreferencesRepository
import org.gaziz.birgram.domain.usecase.chatList.GetChatList
import org.gaziz.birgram.domain.usecase.chatList.LoadAllChats
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    loadAllChats: LoadAllChats,
    getChatList: GetChatList,
    private val chatListRepository: ChatListRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val eventLoopRepository: EventLoopRepository
): ViewModel() {
    init {
        loadAllChats(ChatListType.Main)
    }
    fun logOut(onOk: () -> Unit) {
        eventLoopRepository.logOut {
            onOk()
        }
    }
    val isDark = userPreferencesRepository.isDark.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        true
    )
    fun switchIsDark(data: Boolean){
        viewModelScope.launch {
            userPreferencesRepository.switchIsDark(data)
        }
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