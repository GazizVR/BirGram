package org.gaziz.birgram.features.auth.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gaziz.birgram.core.telegram.internal.updaters.ErrorUpdater
import javax.inject.Inject

class GetErrorMessage @Inject constructor(
    private val tgError: ErrorUpdater
) {
    operator fun invoke(): Flow<String?> {
        return tgError.error.map {
            it?.message
        }
    }
}