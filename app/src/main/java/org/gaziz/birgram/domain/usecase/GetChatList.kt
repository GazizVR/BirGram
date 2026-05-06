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
        return eventLoopRepository.chatList.map { map ->
            map
                .map { it.value }
                .filter { me -> me.positions.find { it.listType == type } != null }
                .sortedByDescending { me -> me.positions.find { it.listType == type }?.isPinned }
                .sortedByDescending { me -> me.positions.find { it.listType == type }?.order }
        }
    }
}