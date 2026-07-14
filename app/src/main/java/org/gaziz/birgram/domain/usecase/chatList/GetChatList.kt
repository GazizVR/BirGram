package org.gaziz.birgram.domain.usecase.chatList

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.domain.model.chat.ChatData
import org.gaziz.birgram.domain.model.chat.ChatListType
import org.gaziz.birgram.core.telegram.EventLoopRepository
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