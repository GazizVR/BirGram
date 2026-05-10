package org.gaziz.birgram.domain.usecase.searchChats

import org.gaziz.birgram.domain.repository.EventLoopRepository
import org.gaziz.birgram.domain.repository.SearchChatsRepository
import javax.inject.Inject

class SendSearchQuery @Inject constructor(
    private val searchChatsRepository: SearchChatsRepository,
    private val eventLoopRepository: EventLoopRepository
) {
    operator fun invoke(
        query: String,
        limit: Int
    ) {
        searchChatsRepository.searchLocal(
            query,
            limit
        ) { chatIds ->
            if(chatIds.isNotEmpty()) {
                searchChatsRepository.updateSearchChats(null)
                chatIds.forEach { chatId ->
                    eventLoopRepository.chatList.value[chatId]?.let {
                        searchChatsRepository.updateSearchChats(it)
                    }
                }
            } else {
                searchChatsRepository.updateSearchChats(null)
            }
        }
    }
}