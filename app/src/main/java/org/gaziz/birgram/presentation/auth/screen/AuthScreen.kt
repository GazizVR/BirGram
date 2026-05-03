package org.gaziz.birgram.presentation.auth.screen

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.domain.model.auth.AuthState
import org.gaziz.birgram.presentation.auth.components.WaitCode
import org.gaziz.birgram.presentation.auth.components.WaitDefault
import org.gaziz.birgram.presentation.auth.components.WaitOther
import org.gaziz.birgram.presentation.auth.components.WaitParams
import org.gaziz.birgram.presentation.auth.components.WaitPassword
import org.gaziz.birgram.presentation.auth.components.WaitPhoneNumber
import org.gaziz.birgram.presentation.auth.viewmodel.AuthViewModel

@Composable
fun AuthScreen() {
    BackHandler{}
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
        AuthState.WaitPhoneNumber -> WaitPhoneNumber({viewModel.setPhoneNumber(it)})
        AuthState.WaitCode -> WaitCode({viewModel.setCode(it)})
        AuthState.WaitPassword -> WaitPassword({viewModel.setPassword(it)})
        is AuthState.Other -> {
            val state = (authState as AuthState.Other).state
            WaitOther(state)
        }
        AuthState.Ready -> { viewModel.switchIsRegister(true) }
    }
}