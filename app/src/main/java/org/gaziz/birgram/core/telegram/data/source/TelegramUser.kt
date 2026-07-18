package org.gaziz.birgram.core.telegram.data.source

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.data.mapper.toStatus
import org.gaziz.birgram.core.telegram.data.mapper.toUser
import org.gaziz.birgram.core.telegram.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramUser @Inject constructor() {
    private val _users = MutableStateFlow<Map<Long, User>>(emptyMap())
    val users = _users.asStateFlow()

    fun onUser(u: TdApi.UpdateUser) {
        _users.update { old ->
            old + (u.user.id to u.user.toUser())
        }
    }
    fun onUserStatus(u: TdApi.UpdateUserStatus){
        _users.update { old ->
            val user = old[u.userId]
            if(user != null) {
                old + (u.userId to user.copy(status = u.status.toStatus()))
            } else {
                val newUser = User(
                    id = u.userId,
                    status = u.status.toStatus()
                )
                old + (u.userId to newUser)
            }
        }
    }
}