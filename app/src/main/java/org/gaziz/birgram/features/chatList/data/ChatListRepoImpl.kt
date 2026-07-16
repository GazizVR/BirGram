package org.gaziz.birgram.features.chatList.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.ClientManager
import org.gaziz.birgram.core.telegram.model.RequestResponse
import org.gaziz.birgram.features.chatList.domain.model.ChatData
import org.gaziz.birgram.features.chatList.domain.model.ChatListType
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import javax.inject.Inject

class ChatListRepoImpl @Inject constructor(
    private val manager: ClientManager
): ChatListRepository {

    private val _chats = MutableStateFlow(emptyMap<Long, ChatData>())
    override val chats: StateFlow<Map<Long, ChatData>> = _chats.asStateFlow()

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
                onError(it)
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

    override fun logOut(onOk: () -> Unit) {
        manager.sendRequest(
            TdApi.LogOut(),
            {},
        ) {
            if(it is TdApi.Ok) {
                _chats.value = emptyMap()
                onOk()
            }
        }
    }
}