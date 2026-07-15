package org.gaziz.birgram.features.chatList.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.features.chatList.domain.model.ChatData
import org.gaziz.birgram.features.chatList.domain.model.ChatListType
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import javax.inject.Inject

class GetChatList @Inject constructor(
    private val chatListRepository: ChatListRepository
) {
    operator fun invoke(type: ChatListType): Flow<List<ChatData>> {
        return chatListRepository.chats.map { map ->
            map
                .map { it.value }
                .filter { me -> me.positions.find { it.listType == type } != null }
                .sortedByDescending { me -> me.positions.find { it.listType == type }?.isPinned }
                .sortedByDescending { me -> me.positions.find { it.listType == type }?.order }
        }
    }
}