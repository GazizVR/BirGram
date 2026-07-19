package org.gaziz.birgram.features.searchChats.domain.usecase

import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import javax.inject.Inject

class SearchLocalChatsUseCase @Inject constructor(
    private val chatService: ChatService,
    private val chatSearchRepository: ChatSearchRepository
) {
    operator fun invoke(
        query: String,
        limit: Int
    ) {
        chatService.searchChatsLocal(
            query,
            limit
        ) { chatSearchRepository.replace(it) }
    }
}