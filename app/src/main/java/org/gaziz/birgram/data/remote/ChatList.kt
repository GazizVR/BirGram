package org.gaziz.birgram.data.remote

import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.chatList.ChatListType
import org.gaziz.birgram.domain.repository.ChatListRepository

class ChatList(val client: Client): ChatListRepository {
    override fun loadChats(
        type: ChatListType,
        limit: Int,
        onError: (String) -> Unit
    ) {
        val query = TdApi.LoadChats().apply {
            this.limit = limit
            this.chatList = when(type) {
                ChatListType.Archive -> TdApi.ChatListArchive()
                is ChatListType.Folder -> TdApi.ChatListFolder().apply {
                    this.chatFolderId = type.folderId
                }
                ChatListType.Main -> TdApi.ChatListMain()
            }
        }
        Helper.sendRequest(query, onError, client = client)
    }

    override fun downloadFile(
        fileId: Int,
        priority: Int,
        offset: Long,
        limit: Long,
        synchronous: Boolean,
        onError: (String) -> Unit
    ) {
        val query = TdApi.DownloadFile().apply {
            this.fileId = fileId
            this.priority = priority
            this.offset = offset
            this.limit = limit
            this.synchronous = synchronous
        }
        Helper.sendRequest(query, onError, client = client)
    }
}