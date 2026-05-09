package org.gaziz.birgram.presentation.searchChats.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.gaziz.birgram.domain.repository.SearchChatsRepository
import org.gaziz.birgram.domain.usecase.SendSearchQuery
import javax.inject.Inject

@HiltViewModel
class SearchChatsViewModel @Inject constructor(
    private val sendSearchQuery: SendSearchQuery,
    private val searchChatsRepository: SearchChatsRepository
): ViewModel() {
    val searchChats = searchChatsRepository.searchedChats
    fun sendSearchQuery(
        query: String
    ) {
        sendSearchQuery(query,20)
    }
    fun downloadPhoto(fileId: Int) {
        searchChatsRepository.downloadPhoto(fileId)
    }
}