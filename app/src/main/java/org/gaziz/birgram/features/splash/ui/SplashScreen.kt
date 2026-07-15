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
import org.gaziz.birgram.features.auth.domain.model.AuthState

@Composable
fun SplashScreen(
    onReady: () -> Unit,
    onAuth: () -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val viewModel = hiltViewModel<SplashViewModel>()
    val localState by viewModel.localState.collectAsState()
    val context = LocalContext.current

    var isSetParams = false
    LaunchedEffect(localState) {
        if(localState == null){
            viewModel.loadState()
        } else {
            if(localState is AuthState.WaitParams && !isSetParams) {
                isSetParams = true
                viewModel.setParams(
                    "${context.filesDir.absolutePath}/tdlib",
                    { isSetParams = false }
                )
            }
        }
    }

    val remoteState by viewModel.remoteState.collectAsState()
    LaunchedEffect(remoteState) {
        if(remoteState is AuthState.Ready){
            onReady()
            return@LaunchedEffect
        }
        if(remoteState is AuthState.Closed && !isSetParams) {
            viewModel.initEventLoop().let {
                viewModel.setParams(
                    "${context.filesDir.absolutePath}/tdlib",
                    { isSetParams = false }
                )
            }
        }
        if(
            remoteState !is AuthState.WaitParams &&
            remoteState !is AuthState.LoggingOut
        ) {
            onAuth()
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
