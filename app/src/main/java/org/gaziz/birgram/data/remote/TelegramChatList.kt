package org.gaziz.birgram.data.remote

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.model.chatList.RequestResponse
import org.gaziz.birgram.domain.repository.ChatListRepository
import javax.inject.Inject

class TelegramChatList @Inject constructor(
    private val manager: TelegramManager
): ChatListRepository {
    override fun loadChats(
        limit: Int,
        listType: ChatListType
    ): RequestResponse {
        var response: RequestResponse = RequestResponse.OK
        manager.sendRequest(
            TdApi.LoadChats().apply {
                this.limit = limit
                this.chatList  = when(listType) {
                    is ChatListType.Folder -> TdApi.ChatListFolder(listType.id)
                    ChatListType.Archive ->  TdApi.ChatListArchive()
                    ChatListType.Main -> TdApi.ChatListMain()
                }
            }
        ) {
            if(it != null) {
                response = it
            }
        }
        return response
    }
}