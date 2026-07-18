package org.gaziz.birgram.features.chatList.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.chats.api.model.ChatListType
import org.gaziz.birgram.core.telegram.error.api.model.ResponseData
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import javax.inject.Inject

class LoadAllChats @Inject constructor(
    private val chatListRepository: ChatListRepository
) {
    operator fun invoke(type: ChatListType) {
        CoroutineScope(Dispatchers.IO).launch {
            var response: ResponseData = ResponseData.OK
            while(response is ResponseData.OK) {
                chatListRepository.loadChats(
                    100,
                    type
                ) { response = it }
                delay(500)
            }
        }
    }
}