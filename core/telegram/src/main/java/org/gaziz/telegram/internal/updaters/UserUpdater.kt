package org.gaziz.telegram.internal.updaters

import org.drinkless.tdlib.TdApi
import org.gaziz.telegram.api.UserService
import org.gaziz.telegram.internal.mapper.toStatus
import org.gaziz.telegram.internal.mapper.toUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUpdater @Inject constructor(
    private val userService: UserService
) {

    fun onUserUpdate(u: TdApi.UpdateUser) {
        userService.updateUsers { old ->
            old + (u.user.id to u.user.toUser())
        }
    }
    fun onUserStatusUpdate(u: TdApi.UpdateUserStatus){
        userService.updateUsers { old ->
            val user = old[u.userId] ?: return@updateUsers old
            old + (u.userId to user.copy(status = u.status.toStatus()))
        }
    }
    fun onLoggingOut() {
        userService.updateUsers { emptyMap() }
    }
}