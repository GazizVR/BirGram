package org.gaziz.birgram.features.searchChats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.model.User
import org.gaziz.birgram.core.telegram.usecase.GetUserById
import org.gaziz.birgram.features.searchChats.domain.repository.SearchChatsRepository
import javax.inject.Inject

@HiltViewModel
class SearchChatsViewModel @Inject constructor(
    private val getUserById: GetUserById,
    private val searchChatsRepository: SearchChatsRepository
): ViewModel() {
    val user: (Long) -> StateFlow<User?> = {
        getUserById(it).stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
    }
    val searchChats = searchChatsRepository.searchedChats
    fun sendSearchQuery(
        query: String
    ) {
        searchChatsRepository.searchLocal(query,20)
    }
    fun downloadChatIcon(
        chatId: Long,
        fileId: Int
    ) {
        searchChatsRepository.downloadChatIcon(chatId,fileId)
    }
}