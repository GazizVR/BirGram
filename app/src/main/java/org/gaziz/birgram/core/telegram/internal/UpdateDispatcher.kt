package org.gaziz.birgram.core.telegram.internal

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.auth.impl.AuthDataSource
import org.gaziz.birgram.core.telegram.chats.impl.TelegramChat
import org.gaziz.birgram.core.telegram.error.impl.TelegramError
import org.gaziz.birgram.core.telegram.messages.impl.TelegramMessage
import org.gaziz.birgram.core.telegram.users.impl.TelegramUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateDispatcher @Inject constructor(
    private val tgAuth: AuthDataSource,
    private val tgChat: TelegramChat,
    private val tgMessage: TelegramMessage,
    private val tgError: TelegramError,
    private val tgUser: TelegramUser
) {
    fun dispatch(u: TdApi.Object){
        when(u) {
            is TdApi.Error -> tgError.onError(u)
            is TdApi.UpdateAuthorizationState -> {
                if(u.authorizationState is TdApi.AuthorizationStateLoggingOut) {
                    tgMessage.onLoggingOut()
                    tgChat.onLoggingOut()
                }
                tgAuth.onUpdateAuthState(u)
            }

            is TdApi.UpdateNewMessage -> tgMessage.onNewUpdate(u)
            is TdApi.UpdateMessageSendSucceeded -> tgMessage.onSendSucceedUpdate(u)

            is TdApi.UpdateNewChat -> tgChat.onNewUpdate(u)
            is TdApi.UpdateChatTitle -> tgChat.onTitleUpdate(u)
            is TdApi.UpdateChatPhoto -> tgChat.onPhotoUpdate(u)

            is TdApi.UpdateChatPosition -> tgChat.onPositionUpdate(u)
            is TdApi.UpdateChatLastMessage -> tgChat.onLastMsgUpdate(u)
            is TdApi.UpdateChatDraftMessage -> tgChat.onDraftMsgUpdate(u)

            is TdApi.UpdateChatReadInbox -> tgChat.onInboxUpdate(u)
            is TdApi.UpdateChatUnreadReactionCount -> tgChat.onReactionCountUpdate(u)
            is TdApi.UpdateChatUnreadMentionCount -> tgChat.onMentionCountUpdate(u)

            is TdApi.UpdateUser -> tgUser.onUser(u)
            is TdApi.UpdateUserStatus -> tgUser.onUserStatus(u)

        }
    }
}