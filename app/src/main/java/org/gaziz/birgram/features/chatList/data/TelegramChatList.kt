package org.gaziz.birgram.features.chatList.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.core.telegram.domain.model.RequestResponse
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatData
import org.gaziz.birgram.core.telegram.domain.model.chat.ChatListType
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import javax.inject.Inject

class TelegramChatList @Inject constructor(
    private val manager: TelegramManager
): ChatListRepository {

    private val _chatList = MutableStateFlow(emptyMap<Long, ChatData>())
    override val chatList: StateFlow<Map<Long, ChatData>> = _chatList.asStateFlow()

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
    override fun logOut(onOk: () -> Unit) {
        manager.sendRequest(
            TdApi.LogOut(),
            {},
        ) {
            if(it is TdApi.Ok) {
                _chatList.value = emptyMap()
                _messages.value = emptyMap()
                onOk()
            }
        }
    }
}