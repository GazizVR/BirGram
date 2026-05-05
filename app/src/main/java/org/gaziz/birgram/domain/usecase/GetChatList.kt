package org.gaziz.birgram.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.domain.model.chatList.ChatData
import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.repository.EventLoopRepository
import javax.inject.Inject

class GetChatList @Inject constructor(
    private val eventLoopRepository: EventLoopRepository
) {
    operator fun invoke(type: ChatListType): Flow<List<ChatData>> {
        return eventLoopRepository.chatList.map { m ->
            m.toList().apply{
                filter { me -> me.second.positions.any { it == type } }
                sortedByDescending { me -> me.second.positions.find { it == type }?.order }
                sortedByDescending { me -> me.second.positions.find { it == type }?.isPinned }
            }.toMap().values.toList()
        }
    }
}