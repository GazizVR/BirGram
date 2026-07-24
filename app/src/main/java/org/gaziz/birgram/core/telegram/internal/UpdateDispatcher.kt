package org.gaziz.birgram.core.telegram.internal

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.updaters.AuthUpdater
import org.gaziz.birgram.core.telegram.internal.updaters.ChatUpdater
import org.gaziz.birgram.core.telegram.internal.updaters.ErrorUpdater
import org.gaziz.birgram.core.telegram.internal.updaters.GroupUpdater
import org.gaziz.birgram.core.telegram.internal.updaters.MessageUpdater
import org.gaziz.birgram.core.telegram.internal.updaters.UserUpdater
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateDispatcher @Inject constructor(
    private val authUpdater: AuthUpdater,
    private val chatUpdater: ChatUpdater,
    private val messageUpdater: MessageUpdater,
    private val errorUpdater: ErrorUpdater,
    private val userUpdater: UserUpdater,
    private val groupUpdater: GroupUpdater
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
                authUpdater.onAuthStateUpdate(u)
            }

            is TdApi.UpdateNewMessage -> messageUpdater.onNewUpdate(u)
            is TdApi.UpdateMessageSendSucceeded -> messageUpdater.onSendSucceedUpdate(u)

            is TdApi.UpdateNewChat -> chatUpdater.onNewUpdate(u)
            is TdApi.UpdateChatTitle -> chatUpdater.onTitleUpdate(u)
            is TdApi.UpdateChatPhoto -> chatUpdater.onPhotoUpdate(u)

            is TdApi.UpdateChatPosition -> chatUpdater.onPositionUpdate(u)
            is TdApi.UpdateChatLastMessage -> chatUpdater.onLastMsgUpdate(u)
            is TdApi.UpdateChatDraftMessage -> chatUpdater.onDraftMsgUpdate(u)
            is TdApi.UpdateChatPermissions -> chatUpdater.onPermissionsUpdate(u)

            is TdApi.UpdateChatAccentColors -> chatUpdater.onChatAccentColorsUpdate(u)
            is TdApi.UpdateAccentColors -> chatUpdater.onAccentColorsUpdate(u)

            is TdApi.UpdateChatReadInbox -> chatUpdater.onInboxUpdate(u)
            is TdApi.UpdateChatUnreadReactionCount -> chatUpdater.onReactionCountUpdate(u)
            is TdApi.UpdateChatUnreadMentionCount -> chatUpdater.onMentionCountUpdate(u)

            is TdApi.UpdateUser -> userUpdater.onUserUpdate(u)
            is TdApi.UpdateUserStatus -> userUpdater.onUserStatusUpdate(u)

            is TdApi.UpdateBasicGroup -> groupUpdater.onBasicGroupUpdate(u)
            is TdApi.UpdateSupergroup -> groupUpdater.onSuperGroupUpdate(u)

        }
    }
}