package org.gaziz.birgram.core.telegram.api

import kotlinx.coroutines.flow.StateFlow
import org.gaziz.birgram.core.telegram.api.model.ResponseData

interface ErrorService {
    val error: StateFlow<ResponseData.Error?>
    fun setError(e: ResponseData.Error?)
    fun setErrorFromException(e: Throwable)
}