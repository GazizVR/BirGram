package org.gaziz.birgram.features.chatList.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.internal.updaters.TelegramChat
import org.gaziz.birgram.features.chatList.data.mapper.toChatData
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import javax.inject.Inject

class GetChatList @Inject constructor(
    private val tgChat: TelegramChat
) {
    operator fun invoke(type: ChatListType): Flow<List<Chat>> {
        return tgChat.chats.map { map ->
            map
                .map { it.value.toChatData() }
                .filter { me -> me.positions.find { it.listType == type } != null }
                .sortedByDescending { me -> me.positions.find { it.listType == type }?.isPinned }
                .sortedByDescending { me -> me.positions.find { it.listType == type }?.order }
        }
    }
}