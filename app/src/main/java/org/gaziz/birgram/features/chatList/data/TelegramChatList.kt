package org.gaziz.birgram.features.chatList.data

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.domain.model.RequestResponse
import org.gaziz.birgram.domain.model.chat.ChatListType
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import javax.inject.Inject

class TelegramChatList @Inject constructor(
    private val manager: TelegramManager
): ChatListRepository {
    override fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (RequestResponse.Error) -> Unit
    ) {
        manager.sendRequest(
            TdApi.LoadChats().apply {
                this.limit = limit
                this.chatList  = when(listType) {
                    is ChatListType.Folder -> TdApi.ChatListFolder(listType.id)
                    ChatListType.Archive ->  TdApi.ChatListArchive()
                    ChatListType.Main -> TdApi.ChatListMain()
                }
            },
            {
                if(it != null) {
                    onError(it)
                }
            }
        )
    }

    override fun downloadChatPhoto(fileId: Int) {
        manager.sendRequest(
            TdApi.DownloadFile().apply {
                this.fileId = fileId
                this.priority = 32
                this.limit = 0
                this.offset = 0
                this.synchronous = false
            },
            {}
        )
    }
}