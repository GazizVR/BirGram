package org.gaziz.birgram.core.telegram.internal.updaters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelegramError @Inject constructor() {
    private val _error = MutableStateFlow<ResponseData.Error?>(null)
    val error = _error.asStateFlow()

    fun setError(e: ResponseData.Error?) {
        _error.update { e }
    }

    fun onError(e: TdApi.Error) {
        _error.update {
            ResponseData.Error(
                code = e.code,
                message = e.message
            )
        }
    }

    fun onException(e: Throwable) {
        _error.update {
            ResponseData.Error(
                code = 500,
                message = e.localizedMessage ?: e.message ?: "internal error"
            )
        }
    }

}