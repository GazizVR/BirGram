package org.gaziz.birgram.features.auth.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.core.telegram.api.model.auth.AuthState
import org.gaziz.birgram.features.auth.ui.components.OtherState
import org.gaziz.birgram.features.auth.ui.components.WaitCode
import org.gaziz.birgram.features.auth.ui.components.WaitPassword
import org.gaziz.birgram.features.auth.ui.components.WaitPhoneNumber

@Composable
fun AuthScreen(
    onReady: () -> Unit,
    onLogOut: () -> Unit
) {
    BackHandler{}
    val viewModel = hiltViewModel<AuthViewModel>()
    val authState by viewModel.authState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    when(authState) {
        AuthState.WaitPhoneNumber -> {
            WaitPhoneNumber(
                { viewModel.setPhoneNumber(it) },
                errorMessage?.message,
            )
        }
        is AuthState.WaitCode -> WaitCode(
            { viewModel.setCode(it) },
            errorMessage?.message,
            {viewModel.restartAuth()},
            (authState as AuthState.WaitCode).codeInfo,
            { viewModel.resendCode() },
        )
        is AuthState.WaitPassword -> WaitPassword(
            { viewModel.setPassword(it) },
            errorMessage?.message,
            {viewModel.restartAuth()},
            (authState as AuthState.WaitPassword).passwordInfo,
        )

        AuthState.Ready -> {
            onReady()
        }

        AuthState.LoggingOut -> { onLogOut() }
        AuthState.Closed -> { onLogOut() }

        is AuthState.Other -> {
            val state = (authState as AuthState.Other).state
            OtherState(state)
        }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}