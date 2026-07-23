package org.gaziz.birgram.features.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.gaziz.birgram.R
import org.gaziz.birgram.core.telegram.api.model.auth.AuthState

@Composable
fun SplashScreen(
    onReady: () -> Unit,
    onAuth: () -> Unit,
    onNonReady: () -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val viewModel = hiltViewModel<SplashViewModel>()
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    var isInitializing = false
    LaunchedEffect(Unit) {
        viewModel.initApplication(onNonReady)
    }
    LaunchedEffect(authState) {
        if(authState == null){
            viewModel.loadState()
        } else {
            if (authState is AuthState.WaitParams && !isInitializing) {
                isInitializing = true
                viewModel.setParams(
                    "${context.filesDir.absolutePath}/tdlib",
                    { isInitializing = false },
                )
            }
            if (authState is AuthState.Closed && !isInitializing) {
                isInitializing = true
                viewModel.initApplication(onNonReady,true).let {
                    viewModel.setParams(
                        "${context.filesDir.absolutePath}/tdlib",
                        { isInitializing = false },
                    )
                }
            }

            if (authState is AuthState.Ready) {
                onReady()
            }
            if(
                authState !is AuthState.WaitParams &&
                authState !is AuthState.LoggingOut &&
                authState !is AuthState.Closed &&
                authState !is AuthState.Ready
            ) {
                onAuth()
            }
        }
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ){
        Image(
            painterResource(R.drawable.app_launcher),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size((windowInfo.containerSize.width/6).dp)
        )
    }
}
