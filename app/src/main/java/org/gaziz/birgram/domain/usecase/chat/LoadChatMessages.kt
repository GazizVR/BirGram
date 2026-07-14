package org.gaziz.birgram.domain.usecase.chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gaziz.birgram.domain.repository.ChatRepository
import org.gaziz.birgram.core.telegram.EventLoopRepository
import javax.inject.Inject

class LoadChatMessages @Inject constructor(
    private val chatRepository: ChatRepository,
    private val eventLoopRepository: EventLoopRepository
) {
    operator fun invoke(
        chatId: Long,
        lastMessageId: Long = 0,
        onResp: () -> Unit
    ){
        CoroutineScope(Dispatchers.IO).launch {
            chatRepository.getChatMessages(
                chatId,
                lastMessageId
            ) { resp ->
                onResp()
                val map = eventLoopRepository.messages.value.toMutableMap()
                resp.forEach { map[it.id] = it }
                eventLoopRepository.setMessages(map.toMap())
            }
        }
    }
}