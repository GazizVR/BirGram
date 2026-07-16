package org.gaziz.birgram.features.chatList.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.datastore.UserPreferencesRepository
import org.gaziz.birgram.features.chatList.domain.model.ChatListType
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import org.gaziz.birgram.features.chatList.domain.usecase.GetChatList
import org.gaziz.birgram.features.chatList.domain.usecase.LoadAllChats
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    loadAllChats: LoadAllChats,
    getChatList: GetChatList,
    private val chatListRepository: ChatListRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {
    init {
        loadAllChats(ChatListType.Main)
    }
    fun logOut(onOk: () -> Unit) {
        chatListRepository.logOut {
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
    fun downloadChatPhoto(
        chatId: Long,
        fileId: Int
    ) {
       viewModelScope.launch {
           chatListRepository.downloadChatIcon(chatId,fileId)
       }
    }
}