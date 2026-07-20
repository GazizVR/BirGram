package org.gaziz.birgram.features.chatList.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPosition
import javax.inject.Inject

class GetChatList @Inject constructor(
    private val chatService: ChatService
) {
    operator fun invoke(type: ChatListType): Flow<List<Chat>> {
        return chatService.chats.map { map ->
            map.values
                .mapNotNull { chat ->
                    val position = chat.positions.find { it.listType == type } ?: return@mapNotNull null
                    chat to position
                }
                .sortedWith(
                    compareByDescending<Pair<Chat, ChatPosition>> { it.second.isPinned }
                        .thenByDescending { it.second.order }
                )
                .map { it.first }
        }
    }
}