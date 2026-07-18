package org.gaziz.birgram.features.chat.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.internal.updaters.ChatUpdater
import org.gaziz.birgram.features.chat.data.mapper.toChatData
import org.gaziz.birgram.features.chat.domain.model.ChatData
import javax.inject.Inject

class GetChatByID @Inject constructor(
    private val tgChat: ChatUpdater
) {
    operator fun invoke(chatId: Long): Flow<ChatData?> {
        return tgChat.chats.map {
            it[chatId]?.toChatData()
        }
    }
}