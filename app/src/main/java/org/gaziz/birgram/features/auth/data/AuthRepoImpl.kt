package org.gaziz.birgram.features.auth.data

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.updaters.AuthDataSource
import org.gaziz.birgram.core.telegram.internal.updaters.TelegramError
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val manager: ClientManager,
    private val tgAuth: AuthDataSource,
    private val tgError: TelegramError
): AuthRepository {

    override fun setPhoneNumber(
        phoneNumber: String,
    ) {
        val phoneNumber = TdApi.SetAuthenticationPhoneNumber().apply {
            this.phoneNumber = phoneNumber
            this.settings = null
        }
        manager.sendRequest(
            phoneNumber,
            { tgError.setError(it) },
            { if(it is TdApi.Ok) tgError.setError(null) }
        )
    }

    override fun checkCode(
        code: String,
    ) {
        val code = TdApi.CheckAuthenticationCode().apply {
            this.code = code
        }
        manager.sendRequest(
            code,
            { tgError.setError(it) },
            { if(it is TdApi.Ok) tgError.setError(null) }
        )
    }

    override fun resendCode(
        isUser: Boolean,
    ) {
        val parameters = TdApi.ResendAuthenticationCode().apply {
            reason = if (isUser) TdApi.ResendCodeReasonUserRequest()
            else TdApi.ResendCodeReasonVerificationFailed()
        }
        manager.sendRequest(
            parameters,
            { tgError.setError(it) },
            { if(it is TdApi.Ok) tgError.setError(null) }
        )
    }

    override fun checkPassword(password: String) {
        val parameters = TdApi.CheckAuthenticationPassword().apply {
            this.password = password
        }
        manager.sendRequest(
            parameters,
            { tgError.setError(it) },
            { if(it is TdApi.Ok) tgError.setError(null) }
        )
    }

    override fun restartAuth() {
        tgAuth.setState(TdApi.AuthorizationStateWaitPhoneNumber())
    }

}