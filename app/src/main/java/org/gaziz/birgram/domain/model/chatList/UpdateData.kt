package org.gaziz.birgram.domain.model.chatList

data class ChatPhotoInfo(val data: ByteArray)

data class FolderInfo(
    val id: Int,
    val name: String,
    val iconName: String
)

sealed class UpdateData {
    data class UpdateChatPosition(
        val chatId: Long,
        val position: ChatPosition,
    ): UpdateData()

    data class UpdateChatLastMessage(
        val chatId: Long,
        val message: MessageData,
        val positions: List<ChatPosition>
    ): UpdateData()

    data class UpdateChatReadOutbox(
        val chatId: Long,
        val lastReadOutboxMessageId: Long,
    )

    data class UpdateUnreadChatCount(
        val list: ChatListType,
        val total: Int,
        val unread: Int,
    )

    data class UpdateChatPhoto(
        val chatId: Long,
        val photo: ChatPhotoInfo
    )

    data class UpdateChatTitle(
        val chatId: Long,
        val title: String
    )

    data class UpdateChatUnreadMentionCount(
        val chatId: Long,
        val unreadCount: Int
    )
    data class UpdateChatReadInbox(
        val chatId: Long,
        val unreadCount: Int
    )
    data class UpdateChatUnreadReactionCount(
        val chatId: Long,
        val unreadCount: Int
    )
}