package org.gaziz.birgram.core.telegram.internal

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.updater.AuthUpdater
import org.gaziz.birgram.core.telegram.internal.updater.ChatUpdater
import org.gaziz.birgram.core.telegram.internal.updater.ErrorUpdater
import org.gaziz.birgram.core.telegram.internal.updater.GroupUpdater
import org.gaziz.birgram.core.telegram.internal.updater.MessageUpdater
import org.gaziz.birgram.core.telegram.internal.updater.UserUpdater
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateDispatcher @Inject constructor(
    private val authUpdater: AuthUpdater,
    private val chatUpdater: ChatUpdater,
    private val messageUpdater: MessageUpdater,
    private val errorUpdater: ErrorUpdater,
    private val userUpdater: UserUpdater,
    private val groupUpdater: GroupUpdater,
) {
    fun dispatch(u: TdApi.Object){
        when(u) {
            is TdApi.Error -> errorUpdater.onError(u)
            is TdApi.UpdateAuthorizationState -> {
                if(u.authorizationState is TdApi.AuthorizationStateLoggingOut) {
                    userUpdater.onLoggingOut()
                    messageUpdater.onLoggingOut()
                    chatUpdater.onLoggingOut()
                }
                authUpdater.onAuthState(u)
            }

            is TdApi.UpdateNewMessage -> messageUpdater.onNewMessage(u)
            is TdApi.UpdateMessageSendSucceeded -> messageUpdater.onSendSucceeded(u)
            is TdApi.UpdateMessageSendFailed -> messageUpdater.onSendFailed(u)
            is TdApi.UpdateDeleteMessages -> messageUpdater.onDeleteMessages(u)
            is TdApi.UpdateMessageContent -> messageUpdater.onMessageContent(u)

            is TdApi.UpdateNewChat -> chatUpdater.onNewChat(u)
            is TdApi.UpdateChatTitle -> chatUpdater.onTitle(u)
            is TdApi.UpdateChatPhoto -> chatUpdater.onPhoto(u)

            is TdApi.UpdateChatPosition -> chatUpdater.onPosition(u)
            is TdApi.UpdateChatLastMessage -> chatUpdater.onLastMessage(u)
            is TdApi.UpdateChatDraftMessage -> chatUpdater.onDraftMessage(u)
            is TdApi.UpdateChatPermissions -> chatUpdater.onPermissions(u)

            is TdApi.UpdateChatAccentColors -> chatUpdater.onChatAccentColors(u)
            is TdApi.UpdateAccentColors -> chatUpdater.onAccentColors(u)

            is TdApi.UpdateChatReadInbox -> chatUpdater.onInbox(u)
            is TdApi.UpdateChatUnreadReactionCount -> chatUpdater.onReactionCount(u)
            is TdApi.UpdateChatUnreadMentionCount -> chatUpdater.onMentionCount(u)

            is TdApi.UpdateUser -> userUpdater.onUser(u)
            is TdApi.UpdateUserStatus -> userUpdater.onUserStatus(u)

            is TdApi.UpdateBasicGroup -> groupUpdater.onBasicGroup(u)
            is TdApi.UpdateSupergroup -> groupUpdater.onSuperGroup(u)
        }
    }
}