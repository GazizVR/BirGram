package org.gaziz.telegram.internal.updaters

import org.drinkless.tdlib.TdApi
import org.gaziz.telegram.api.ErrorService
import org.gaziz.telegram.api.model.ResponseData
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