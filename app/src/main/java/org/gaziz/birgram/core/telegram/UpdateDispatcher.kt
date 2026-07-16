package org.gaziz.birgram.core.telegram

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.data.TelegramAuth
import org.gaziz.birgram.core.telegram.data.TelegramChat
import org.gaziz.birgram.core.telegram.data.TelegramMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateDispatcher @Inject constructor(
    private val tgAuth: TelegramAuth,
    private val tgChat: TelegramChat,
    private val tgMessage: TelegramMessage
) {
    fun dispatch(u: TdApi.Object){
        when(u) {
            is TdApi.UpdateAuthorizationState -> tgAuth.onUpdateAuthState(u)

            is TdApi.UpdateNewMessage -> tgMessage.onNewUpdate(u)
            is TdApi.UpdateMessageSendSucceeded -> tgMessage.onSendSucceedUpdate(u)

            is TdApi.UpdateNewChat -> tgChat.onNewUpdate(u)
            is TdApi.UpdateChatPosition -> tgChat.onPositionUpdate(u)
            is TdApi.UpdateChatTitle -> tgChat.onTitleUpdate(u)
            is TdApi.UpdateChatPhoto -> tgChat.onPhotoUpdate(u)

            is TdApi.UpdateChatLastMessage -> tgChat.onLastMsgUpdate(u)
            is TdApi.UpdateChatReadInbox -> tgChat.onInboxUpdate(u)
            is TdApi.UpdateChatUnreadReactionCount -> tgChat.onReactionCountUpdate(u)
            is TdApi.UpdateChatUnreadMentionCount -> tgChat.onMentionCountUpdate(u)
        }
    }
}