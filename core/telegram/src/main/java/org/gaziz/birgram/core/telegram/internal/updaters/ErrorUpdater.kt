package org.gaziz.birgram.core.telegram.internal.updaters

import org.drinkless.tdlib.TdApi
import org.gaziz.birgram.core.telegram.api.ErrorService
import org.gaziz.birgram.core.telegram.api.model.ResponseData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorUpdater @Inject constructor(
    private val errorService: ErrorService
) {

    fun onError(e: TdApi.Error) {
        errorService.setError(
            ResponseData.Error(
                code = e.code,
                message = e.message
            )
        )
    }

}