package org.gaziz.birgram.features.auth.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.ClientManager
import org.gaziz.birgram.core.telegram.data.source.TelegramAuth
import org.gaziz.birgram.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val manager: ClientManager,
    private val tgAuth: TelegramAuth
): AuthRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            manager.exception.collect {
                _errorMessage.update { it }
            }
        }
    }

    private val _errorMessage = MutableStateFlow<String?>(null)
    override val errorMessage = _errorMessage.asStateFlow()

    override fun setPhoneNumber(
        phoneNumber: String,
    ) {
        val phoneNumber = TdApi.SetAuthenticationPhoneNumber().apply {
            this.phoneNumber = phoneNumber
            this.settings = null
        }
        manager.sendRequest(
            phoneNumber,
            { _errorMessage.update { it } },
            { if(it is TdApi.Ok) _errorMessage.update { null } }
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
            { _errorMessage.update { it } },
            { if(it is TdApi.Ok) _errorMessage.update { null } }
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
            { _errorMessage.update { it } },
            { if(it is TdApi.Ok) _errorMessage.update { null } }
        )
    }

    override fun checkPassword(password: String) {
        val parameters = TdApi.CheckAuthenticationPassword().apply {
            this.password = password
        }
        manager.sendRequest(
            parameters,
            { _errorMessage.update { it } },
            { if(it is TdApi.Ok) _errorMessage.update { null } }
        )
    }

    override fun restartAuth() {
        tgAuth.setState(TdApi.AuthorizationStateWaitPhoneNumber())
    }

}