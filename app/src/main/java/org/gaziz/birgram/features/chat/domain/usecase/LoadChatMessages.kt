package org.gaziz.birgram.features.chat.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.domain.repository.EventLoopRepository
import org.gaziz.birgram.features.chat.domain.repository.ChatRepository
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
                eventLoopRepository.setMessages { map ->
                    val newMap = map.toMutableMap()
                    resp.forEach { newMap[it.id] = it }
                    newMap.toMap()
                }
            }
        }
    }
}