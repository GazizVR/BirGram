package org.gaziz.birgram.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gaziz.birgram.domain.model.RequestResponse
import org.gaziz.birgram.domain.model.chat.ChatListType
import org.gaziz.birgram.domain.repository.ChatListRepository
import javax.inject.Inject

class LoadAllChats @Inject constructor(
    private val chatListRepository: ChatListRepository
) {
    operator fun invoke(type: ChatListType) {
        CoroutineScope(Dispatchers.IO).launch {
            var response: RequestResponse = RequestResponse.OK
            while(response is RequestResponse.OK) {
                chatListRepository.loadChats(
                    100,
                    type
                ) { response = it }
                delay(500)
            }
        }
    }
}