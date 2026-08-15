package org.gaziz.telegram.api.usecase

import org.gaziz.telegram.api.ErrorService
import org.gaziz.telegram.internal.ClientManager
import org.gaziz.telegram.internal.UpdateDispatcher
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