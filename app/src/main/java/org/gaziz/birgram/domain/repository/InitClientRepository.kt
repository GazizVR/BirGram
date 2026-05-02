package org.gaziz.birgram.domain.repository

import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.domain.model.chatList.ChatData
import org.gaziz.birgram.domain.model.chatList.FileData
import org.gaziz.birgram.domain.model.chatList.FolderInfo
import org.gaziz.birgram.domain.model.chatList.UpdateData

interface InitClientRepository {
    fun initClient(
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

        onFile: (file: FileData) -> Unit,
        onChatFolders: (folders: List<FolderInfo>) -> Unit,
        onUnreadChatCount: (unreadCount: UpdateData.UpdateUnreadChatCount) -> Unit
    )
}