package org.gaziz.searchchats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.gaziz.searchchats.domain.repository.ChatSearchRepository
import org.gaziz.searchchats.domain.usecase.SearchLocalChats
import javax.inject.Inject

@HiltViewModel
class SearchChatsViewModel @Inject constructor(
    chatSearchRepository: ChatSearchRepository,
    private val searchLocalChats: SearchLocalChats
): ViewModel() {
    val searchChats = chatSearchRepository.chats
    fun sendSearchQuery(
        query: String
    ) {
        viewModelScope.launch {
            searchLocalChats(query,20)
        }
    }
}