package org.gaziz.birgram.features.searchChats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.api.usecase.DownloadChatPhotoSmall
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import org.gaziz.birgram.features.searchChats.domain.usecase.SearchLocalChatsUseCase
import javax.inject.Inject

@HiltViewModel
class SearchChatsViewModel @Inject constructor(
    private val chatSearchRepository: ChatSearchRepository,
    private val userService: UserService,
    private val downloadChatPhotoSmall: DownloadChatPhotoSmall,
    private val searchLocalChatsUseCase: SearchLocalChatsUseCase
): ViewModel() {
    fun getUser(userId: Long): StateFlow<User?> {
        return userService.users.map {
            it[userId]
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
    }
    val searchChats = chatSearchRepository.chats
    fun sendSearchQuery(
        query: String
    ) {
        searchLocalChatsUseCase(query,20)
    }
    fun downloadChatIcon(
        chatId: Long,
        fileId: Int
    ) {
        downloadChatPhotoSmall(chatId,fileId)
    }
}