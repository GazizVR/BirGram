package org.gaziz.birgram.core.telegram.internal.updaters

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.internal.mapper.toStatus
import org.gaziz.birgram.core.telegram.internal.mapper.toUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUpdater @Inject constructor(
    private val userService: UserService
) {

    fun onUser(u: TdApi.UpdateUser) {
        userService.updateUsers { old ->
            old + (u.user.id to u.user.toUser())
        }
    }
    fun onUserStatus(u: TdApi.UpdateUserStatus){
        userService.updateUsers { old ->
            val user = old[u.userId] ?: return@updateUsers old
            old + (u.userId to user.copy(status = u.status.toStatus()))
        }
    }
}