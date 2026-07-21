package org.gaziz.birgram.features.chatList.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import javax.inject.Inject

class LoadChatList @Inject constructor(
    private val chatService: ChatService
) {
    operator fun invoke(type: ChatListType) {
        CoroutineScope(Dispatchers.IO).launch {
            var response: ResponseData = ResponseData.OK
            while(response is ResponseData.OK) {
                chatService.loadChats(
                    100,
                    type
                ) { response = it }
                delay(500)
            }
        }
    }
}