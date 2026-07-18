package org.gaziz.birgram.features.chatList.data

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.chats.impl.TelegramChat
import org.gaziz.birgram.core.telegram.error.api.model.ResponseData
import org.gaziz.birgram.core.telegram.chats.api.model.ChatListType
import org.gaziz.birgram.features.chatList.domain.repository.ChatListRepository
import javax.inject.Inject

class ChatListRepoImpl @Inject constructor(
    private val manager: ClientManager,
    private val tgChat: TelegramChat
): ChatListRepository {

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

    override fun downloadChatIcon(
        chatId: Long,
        fileId: Int
    ) {
        tgChat.downloadChatPhotoSmall(chatId,fileId)
    }

    override fun logOut(onOk: () -> Unit) {
        manager.sendRequest(
            TdApi.LogOut(),
            {},
        ) {
            if(it is TdApi.Ok) {
                onOk()
            }
        }
    }
}