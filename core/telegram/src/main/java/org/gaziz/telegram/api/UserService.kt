package org.gaziz.telegram.api

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.telegram.api.model.user.User

interface UserService {
    val users: StateFlow<Map<Long, User>>
    fun updateUsers(updFun: (Map<Long, User>) -> Map<Long, User>)
}