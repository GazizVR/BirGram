package org.gaziz.birgram.core.telegram.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.model.AccentColor
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatListType
import org.gaziz.birgram.core.telegram.internal.ClientManager
import javax.inject.Inject

class ChatServiceImpl @Inject constructor(
    private val manager: ClientManager,
): ChatService {
    private val _accentColors = MutableStateFlow<Map<Int, AccentColor>>(emptyMap())
    override val accentColors: StateFlow<Map<Int, AccentColor>> = _accentColors.asStateFlow()

    override fun updateAccentColors(
        updFun: (Map<Int, AccentColor>) -> Map<Int, AccentColor>
    ) {
        _accentColors.update(updFun)
    }

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

    override fun searchChatsLocal(
        query: String,
        limit: Int,
        onChats: (Map<Long, Chat>) -> Unit
    ) {
        manager.sendRequest(
            TdApi.SearchChats().apply {
                this.query = query
                this.limit = limit
            },
            { onChats(emptyMap()) },
            {
                if(it is TdApi.Chats) {
                    val map = mutableMapOf<Long,Chat>().apply {
                        for(chatId in it.chatIds) {
                            val chat = chats.value[chatId] ?: continue
                            put(chatId,chat)
                        }
                    }.toMap()
                    onChats(map)
                } else {
                    onChats(emptyMap())
                }
            }
        )
    }
}