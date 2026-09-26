package org.gaziz.birgram.core.telegram.api.usecase

import org.gaziz.birgram.core.telegram.api.ErrorService
import org.gaziz.birgram.core.telegram.internal.ClientManager
import org.gaziz.birgram.core.telegram.internal.UpdateDispatcher
import javax.inject.Inject

class InitClient @Inject constructor(
    private val manager: ClientManager,
    private val errorService: ErrorService,
    private val updateDispatcher: UpdateDispatcher
) {
    operator fun invoke(
        force: Boolean
    ) {
        if(!manager.isClientActive() || force) {
            manager.createClient(
                onUpdate = updateDispatcher::dispatch,
                onException = errorService::setErrorFromException
            )
        }
    }
}