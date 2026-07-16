package org.gaziz.birgram.features.searchChats.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gaziz.birgram.features.searchChats.domain.repository.SearchChatsRepository
import javax.inject.Inject

@HiltViewModel
class SearchChatsViewModel @Inject constructor(
    private val searchChatsRepository: SearchChatsRepository
): ViewModel() {
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