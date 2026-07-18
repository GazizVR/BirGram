package org.gaziz.birgram.core.telegram.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.data.source.TelegramUser
import org.gaziz.birgram.core.telegram.model.User
import javax.inject.Inject

class GetUserById @Inject constructor(
    private val tgUser: TelegramUser
) {
    operator fun invoke(
        userId: Long
    ): Flow<User?> {
        return tgUser.users.map {
            it[userId]
        }
    }
}