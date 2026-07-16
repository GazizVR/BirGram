package org.gaziz.birgram.features.splash.domain.repository

interface SplashRepository {
    fun initApplication()

    fun loadAppState()
    fun setParameters(
        databasePath: String,
        onError: (String) -> Unit,
    )
}