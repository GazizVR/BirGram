package org.gaziz.birgram.data.remote

import android.util.Log
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.domain.model.chatList.*
import org.gaziz.birgram.domain.repository.InitClientRepository

class InitClient: InitClientRepository {
    override fun initClient(
        onError: (String) -> Unit,
        onAuth: (AuthState) -> Unit,
        onNewChat: (ChatData) -> Unit,
        onChatPosition: (UpdateData.UpdateChatPosition) -> Unit,
        onChatLastMessage: (UpdateData.UpdateChatLastMessage) -> Unit,
        onChatPhoto: (UpdateData.UpdateChatPhoto) -> Unit,
        onChatTitle: (UpdateData.UpdateChatTitle) -> Unit,
        onMentionCount: (UpdateData.UpdateChatUnreadMentionCount) -> Unit,
        onReactionCount: (UpdateData.UpdateChatUnreadReactionCount) -> Unit,
        onUnreadCount: (UpdateData.UpdateChatReadInbox) -> Unit,
        onOutbox: (UpdateData.UpdateChatReadOutbox) -> Unit,
        onFile: (FileData) -> Unit,
        onChatFolders: (List<FolderInfo>) -> Unit,
        onUnreadChatCount: (UpdateData.UpdateUnreadChatCount) -> Unit
    ) {
        Helper.client = Client.create(
            { event ->
                when (event) {
                    is TdApi.Error -> {
                        Log.e("TDLib", "${event.code}: ${event.message}")
                        onError(event.message)
                    }
                    is TdApi.UpdateAuthorizationState -> {
                        val authState = when (event.authorizationState) {
                            is TdApi.AuthorizationStateWaitTdlibParameters -> AuthState.Parameters
                            is TdApi.AuthorizationStateWaitPhoneNumber -> AuthState.PhoneNumber
                            is TdApi.AuthorizationStateWaitCode -> AuthState.Code
                            is TdApi.AuthorizationStateWaitPassword -> AuthState.Password
                            is TdApi.AuthorizationStateWaitEmailAddress -> AuthState.Email
                            is TdApi.AuthorizationStateWaitEmailCode -> AuthState.EmailCode
                            is TdApi.AuthorizationStateWaitRegistration -> AuthState.Registration
                            is TdApi.AuthorizationStateReady -> AuthState.Ready
                            else -> return@create
                        }
                        onAuth(authState)
                    }
                    is TdApi.UpdateFile -> {
                        onFile(FileData(event.file.id, event.file.size))
                    }
                    is TdApi.UpdateChatPosition -> {
                        val list = when (event.position.list) {
                            is TdApi.ChatListMain -> ChatListType.Main
                            is TdApi.ChatListArchive -> ChatListType.Archive
                            else -> ChatListType.Folder(
                                (event.position.list as TdApi.ChatListFolder).chatFolderId
                            )
                        }
                        onChatPosition(
                            UpdateData.UpdateChatPosition(
                                chatId = event.chatId,
                                position = ChatPosition(
                                    order = event.position.order,
                                    list = list,
                                    isPinned = event.position.isPinned
                                )
                            )
                        )
                    }
                    is TdApi.UpdateNewChat -> {
                        onNewChat(
                            ChatData(
                                id = event.chat.id,
                                title = event.chat.title,
                            )
                        )
                    }
                    is TdApi.UpdateChatLastMessage -> {
                        onChatLastMessage(
                            UpdateData.UpdateChatLastMessage(
                                chatId = event.chatId,
                                message = MessageData(
                                    event.lastMessage?.id ?: 0
                                ),
                                positions = event.positions.map { pos ->
                                    val list = when (pos.list) {
                                        is TdApi.ChatListMain -> ChatListType.Main
                                        is TdApi.ChatListArchive -> ChatListType.Archive
                                        else -> ChatListType.Folder(
                                            (pos.list as TdApi.ChatListFolder).chatFolderId
                                        )
                                    }
                                    ChatPosition(
                                        order = pos.order,
                                        list = list,
                                        isPinned = pos.isPinned,

                                    )
                                }
                            )
                        )
                    }
                    is TdApi.UpdateChatReadOutbox -> {
                        onOutbox(
                            UpdateData.UpdateChatReadOutbox(
                                chatId = event.chatId,
                                lastReadOutboxMessageId = event.lastReadOutboxMessageId
                            )
                        )
                    }
                    is TdApi.UpdateChatPhoto -> {
                        onChatPhoto(
                            UpdateData.UpdateChatPhoto(
                                chatId = event.chatId,
                                photo = ChatPhotoInfo(
                                    data = event.photo?.minithumbnail?.data ?: byteArrayOf()
                                )
                            )
                        )
                    }
                    is TdApi.UpdateChatTitle -> {
                        onChatTitle(
                            UpdateData.UpdateChatTitle(
                                chatId = event.chatId,
                                title = event.title
                            )
                        )
                    }
                    is TdApi.UpdateChatUnreadMentionCount -> {
                        onMentionCount(
                            UpdateData.UpdateChatUnreadMentionCount(
                                chatId = event.chatId,
                                unreadCount = event.unreadMentionCount
                            )
                        )
                    }
                    is TdApi.UpdateChatUnreadReactionCount -> {
                        onReactionCount(
                            UpdateData.UpdateChatUnreadReactionCount(
                                chatId = event.chatId,
                                unreadCount = event.unreadReactionCount
                            )
                        )
                    }
                    is TdApi.UpdateChatReadInbox -> {
                        onUnreadCount(
                            UpdateData.UpdateChatReadInbox(
                                chatId = event.chatId,
                                unreadCount = event.unreadCount
                            )
                        )
                    }
                    is TdApi.UpdateChatFolders -> {
                        val folders = event.chatFolders.map { folder ->
                            FolderInfo(
                                id = folder.id,
                                name = folder.name.text.text,
                                iconName = folder.icon.name
                            )
                        }
                        onChatFolders(folders)
                    }
                    is TdApi.UpdateUnreadChatCount -> {
                        val list = when (event.chatList) {
                            is TdApi.ChatListMain -> ChatListType.Main
                            is TdApi.ChatListArchive -> ChatListType.Archive
                            else -> ChatListType.Folder(
                                (event.chatList as TdApi.ChatListFolder).chatFolderId
                            )
                        }
                        onUnreadChatCount(
                            UpdateData.UpdateUnreadChatCount(
                                list = list,
                                total = event.totalCount,
                                unread = event.unreadCount
                            )
                        )
                    }
                }
            },
            { throwable ->
                val message = throwable.localizedMessage ?: throwable.message ?: "unknown update handler exception"
                Log.e("TDLib", message)
                onError(message)
            },
            null
        )
    }
}