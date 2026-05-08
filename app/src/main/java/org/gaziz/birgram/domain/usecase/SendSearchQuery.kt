package org.gaziz.birgram.domain.usecase

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
        val chatIds = searchChatsRepository.searchLocal(query,limit)
        if(chatIds.isNotEmpty()) {
            searchChatsRepository.updateSearchChats(null)
            chatIds.forEach {
                val chat = eventLoopRepository.chatList.value[it]
                searchChatsRepository.updateSearchChats(chat)
            }
        } else {
           searchChatsRepository.updateSearchChats(null)
        }
    }
}