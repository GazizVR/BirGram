package org.gaziz.birgram.presentation.auth.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.presentation.auth.components.WaitDefault
import org.gaziz.birgram.presentation.auth.components.WaitParams
import org.gaziz.birgram.presentation.auth.viewmodel.AuthViewModel

@Composable
fun AuthScreen() {
    val viewModel = hiltViewModel<AuthViewModel>()
    val authState by viewModel.authState.collectAsState()
    val isRegister by viewModel.isRegistered.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    when(authState) {
        AuthState.WaitParams -> {
            if(!isRegister){
                WaitParams(
                    { viewModel.setParams(it) },
                    {viewModel.switchTheme(!isDarkTheme)},
                    isDarkTheme
                )
            } else {
                WaitDefault()
            }
        }
        AuthState.WaitPhoneNumber -> TODO()
        AuthState.WaitCode -> TODO()
        AuthState.WaitPassword -> TODO()
        is AuthState.Other -> TODO()
        AuthState.Ready -> TODO()
    }
}