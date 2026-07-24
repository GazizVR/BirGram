package org.gaziz.birgram.features.searchChats.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import org.gaziz.birgram.features.searchChats.domain.usecase.SearchLocalChats
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
        searchLocalChats(query,20)
    }
}