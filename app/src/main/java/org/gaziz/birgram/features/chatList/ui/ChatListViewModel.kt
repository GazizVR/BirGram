package org.gaziz.birgram.features.chatList.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.datastore.UserPreferencesRepository
import org.gaziz.birgram.core.telegram.api.AuthService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import org.gaziz.birgram.features.chatList.domain.usecase.GetChatList
import org.gaziz.birgram.features.chatList.domain.usecase.LoadChatList
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    loadChatList: LoadChatList,
    getChatList: GetChatList,
    private val authService: AuthService,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {
    init {
        loadChatList(ChatListType.Main)
        loadChatList(ChatListType.Archive)
    }
    fun logOut(onOk: () -> Unit) {
        authService.logOut {
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
    val archiveChatList = getChatList(ChatListType.Archive).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )
}