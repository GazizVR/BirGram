package org.gaziz.birgram.features.auth.data

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.TelegramManager
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class TelegramAuth @Inject constructor(
    private val manager: TelegramManager
): AuthRepository {

    override fun setPhoneNumber(
        phoneNumber: String,
        onError: (String?) -> Unit
    ) {
        val phoneNumber = TdApi.SetAuthenticationPhoneNumber().apply {
            this.phoneNumber = phoneNumber
            this.settings = null
        }
        manager.sendRequest(phoneNumber,{ if (it != null) onError(it.message) else onError(null) })
    }

    override fun checkCode(
        code: String,
        onError: (String?) -> Unit
    ) {
        val code = TdApi.CheckAuthenticationCode().apply {
            this.code = code
        }
        manager.sendRequest(code,{ if (it != null) onError(it.message) else onError(null) })
    }

    override fun resendCode(
        isUser: Boolean,
        onError: (String?) -> Unit
    ) {
        val parameters = TdApi.ResendAuthenticationCode().apply {
            reason = if (isUser) TdApi.ResendCodeReasonUserRequest()
            else TdApi.ResendCodeReasonVerificationFailed()
        }
        manager.sendRequest(parameters,{ if (it != null) onError(it.message) else onError(null) })
    }

    override fun checkPassword(
        password: String,
        onError: (String?) -> Unit
    ) {
        val parameters = TdApi.CheckAuthenticationPassword().apply {
            this.password = password
        }
        manager.sendRequest(parameters,{ if (it != null) onError(it.message) else onError(null) })
    }

}