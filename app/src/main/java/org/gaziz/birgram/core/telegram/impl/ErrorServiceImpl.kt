package org.gaziz.birgram.core.telegram.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gaziz.birgram.core.telegram.api.ErrorService
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import javax.inject.Inject

class ErrorServiceImpl @Inject constructor(): ErrorService {
    private val _error = MutableStateFlow<ResponseData.Error?>(null)
    override val error = _error.asStateFlow()

    override fun setError(e: ResponseData.Error?) {
        _error.update { e }
    }

    override fun setErrorFromException(e: Throwable) {
        setError(
            ResponseData.Error(
                code = 500,
                message = e.localizedMessage ?: e.message ?: "internal error"
            )
        )
    }

}