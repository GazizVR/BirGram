package org.gaziz.birgram.core.telegram.api

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.api.model.user.User

interface UserService {
    val users: StateFlow<Map<Long, User>>
    fun updateUsers(updFun: (Map<Long,User>) -> Map<Long,User>)
}