package org.gaziz.birgram.core.telegram.internal.updaters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.internal.mapper.toStatus
import org.gaziz.birgram.core.telegram.internal.mapper.toUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUpdater @Inject constructor() {
    private val _users = MutableStateFlow<Map<Long, User>>(emptyMap())
    val users = _users.asStateFlow()

    fun onUser(u: TdApi.UpdateUser) {
        _users.update { old ->
            old + (u.user.id to u.user.toUser())
        }
    }
    fun onUserStatus(u: TdApi.UpdateUserStatus){
        _users.update { old ->
            val user = old[u.userId] ?: return@update old
            old + (u.userId to user.copy(status = u.status.toStatus()))
        }
    }
}