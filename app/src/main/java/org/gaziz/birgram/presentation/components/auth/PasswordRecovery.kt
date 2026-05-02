package org.gaziz.birgram.presentation.components.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

sealed class RecoveryState {
    object CodeCheck: RecoveryState()
}

@Composable
fun PasswordRecovery(){
    var currentState by rememberSaveable { mutableStateOf(RecoveryState.CodeCheck) }
    AnimatedContent(
        targetState = currentState,
        transitionSpec = {
            slideInHorizontally(tween(300,50),{it}).togetherWith(shrinkHorizontally(tween(300,50), targetWidth = {-it}))
        }
    ){
        when(it){
            else -> {

            }
        }
    }
}