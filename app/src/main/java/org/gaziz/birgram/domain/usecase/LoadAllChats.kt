package org.gaziz.birgram.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.model.RequestResponse
import org.gaziz.birgram.domain.repository.ChatListRepository
import javax.inject.Inject

class LoadAllChats @Inject constructor(
    private val chatListRepository: ChatListRepository
) {
    operator fun invoke(type: ChatListType) {
        CoroutineScope(Dispatchers.IO).launch {
            while(true) {
                val response = chatListRepository.loadChats(100,type)
                if(response is RequestResponse.Error) {
                    break
                }
                delay(500)
            }
        }
    }
}