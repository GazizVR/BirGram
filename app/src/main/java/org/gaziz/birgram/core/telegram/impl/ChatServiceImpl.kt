package org.gaziz.birgram.core.telegram.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import org.gaziz.birgram.core.telegram.api.model.chat.ChatPhoto
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.mapper.toFileData
import javax.inject.Inject

class ChatServiceImpl @Inject constructor(
    private val manager: ClientManager
): ChatService {
    private val _chats = MutableStateFlow<Map<Long, Chat>>(emptyMap())
    override val chats: StateFlow<Map<Long, Chat>> = _chats.asStateFlow()

    override fun updateChats(
        updFun: (Map<Long, Chat>) -> Map<Long, Chat>
    ) {
        _chats.update(updFun)
    }

    override fun openChat(
        chatId: Long,
        onOK: () -> Unit
    ) {
        manager.sendRequest(
            TdApi.OpenChat().apply { this.chatId = chatId },
        ) {
            if(it is TdApi.Ok) { onOK() }
        }
    }

    override fun closeChat(chatId: Long) {
        manager.sendRequest(
            TdApi.CloseChat().apply { this.chatId = chatId },
        )
    }

    override fun loadChats(
        limit: Int,
        listType: ChatListType,
        onError: (ResponseData.Error) -> Unit
    ) {
        manager.sendRequest(
            TdApi.LoadChats().apply {
                this.limit = limit
                this.chatList = when(listType) {
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

    override fun downloadChatIcon(chatId: Long, fileId: Int) {
        manager.sendRequest(
            TdApi.DownloadFile().apply {
                this.fileId = fileId
                this.priority = 32
                this.limit = 0
                this.offset = 0
                this.synchronous = true
            },
            {},
            { obj ->
                if(obj is TdApi.File) {
                    _chats.update { old ->
                        val chat = old[chatId] ?: return@update old
                        val chatPhoto = chat.photo
                        var photo = ChatPhoto(
                            miniThumbnail = null,
                            small = obj.toFileData()
                        )
                        if(chatPhoto != null) { photo = chatPhoto.copy(small = obj.toFileData()) }
                        old + (chatId to chat.copy(photo = photo))
                    }
                }
            }
        )
    }
}