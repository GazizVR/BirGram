package org.gaziz.birgram.presentation.auth.screen

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
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.presentation.auth.components.OtherState
import org.gaziz.birgram.presentation.auth.components.WaitCode
import org.gaziz.birgram.presentation.auth.components.WaitPassword
import org.gaziz.birgram.presentation.auth.components.WaitPhoneNumber
import org.gaziz.birgram.presentation.auth.viewmodel.AuthViewModel

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
                errorMessage,
            )
        }
        is AuthState.WaitCode -> WaitCode(
            { viewModel.setCode(it) },
            errorMessage,
            {viewModel.restartAuth()},
            (authState as AuthState.WaitCode).codeInfo,
            { viewModel.resendCode() },
        )
        is AuthState.WaitPassword -> WaitPassword(
            { viewModel.setPassword(it) },
            errorMessage,
            {viewModel.restartAuth()},
            (authState as AuthState.WaitPassword).passwordInfo,
        )
        AuthState.Ready -> {
            onReady()
        }
        is AuthState.Other -> {
            val state = (authState as AuthState.Other).state
            OtherState(state)
        }
        AuthState.LoggingOut -> { onLogOut() }
        AuthState.Closed -> { onLogOut() }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}