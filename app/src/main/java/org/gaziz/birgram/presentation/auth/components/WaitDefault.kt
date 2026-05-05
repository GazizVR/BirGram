package org.gaziz.birgram.presentation.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.gaziz.birgram.R

@Composable
fun WaitDefault(
    onInit: (String) -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        delay(500)
        onInit("${context.filesDir.absolutePath}/tdlib")
    }
    Box(Modifier.fillMaxSize()){
        Image(
            painterResource(R.drawable.app_launcher),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size((windowInfo.containerSize.width/6).dp)
        )
    }
}
