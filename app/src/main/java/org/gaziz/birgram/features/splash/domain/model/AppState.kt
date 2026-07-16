package org.gaziz.birgram.features.splash.domain.model

sealed interface AppState {
    object Init: AppState
    object Auth: AppState
    object Ready: AppState

    object Stopping: AppState
    object Stopped: AppState
}
