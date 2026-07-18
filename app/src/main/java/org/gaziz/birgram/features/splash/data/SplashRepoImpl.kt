package org.gaziz.birgram.features.splash.data

import org.gaziz.birgram.features.splash.domain.repository.SplashRepository
import javax.inject.Inject

class SplashRepoImpl @Inject constructor(
): SplashRepository {
    override fun initApplication() {
    }
    override fun loadAppState() {
    }

    override fun setParameters(
        databasePath: String,
        onError: (String) -> Unit
    ) {
    }
}