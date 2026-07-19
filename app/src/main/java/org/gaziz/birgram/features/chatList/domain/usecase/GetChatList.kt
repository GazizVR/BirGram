package org.gaziz.birgram.features.chatList.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import javax.inject.Inject

class GetChatList @Inject constructor(
    private val chatService: ChatService
) {
    operator fun invoke(type: ChatListType): Flow<List<Chat>> {
        return chatService.chats.map { map ->
            map
                .map { it.value }
                .filter { me -> me.positions.find { it.listType == type } != null }
                .sortedByDescending { me -> me.positions.find { it.listType == type }?.isPinned }
                .sortedByDescending { me -> me.positions.find { it.listType == type }?.order }
        }
    }
}