package org.gaziz.birgram.core.telegram.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.user.User
import javax.inject.Inject

class UserServiceImpl @Inject constructor(): UserService {

    private val _users = MutableStateFlow<Map<Long, User>>(emptyMap())
    override val users: StateFlow<Map<Long, User>> = _users.asStateFlow()

    override fun updateUsers(
        updFun: (Map<Long,User>) -> Map<Long,User>
    ) {
        _users.update(updFun)
    }
}