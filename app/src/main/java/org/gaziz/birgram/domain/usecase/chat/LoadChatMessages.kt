package org.gaziz.birgram.domain.usecase.chat

import org.gaziz.birgram.domain.repository.ChatRepository
import org.gaziz.birgram.domain.repository.EventLoopRepository
import javax.inject.Inject

class LoadChatMessages @Inject constructor(
    private val chatRepository: ChatRepository,
    private val eventLoopRepository: EventLoopRepository
) {
    operator fun invoke(
        chatId: Long,
        lastMessageId: Long = 0
    ){
        chatRepository.getChatMessages(
            chatId,
            lastMessageId
        ) { resp ->
            val map = eventLoopRepository.messages.value.toMutableMap()
            resp.forEach { map[it.id] = it }
            eventLoopRepository.setMessages(map.toMap())
        }
    }
}