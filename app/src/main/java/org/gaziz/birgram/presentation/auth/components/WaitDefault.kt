package org.gaziz.birgram.presentation.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.gaziz.birgram.R

@Composable
fun WaitDefault() {
    val windowInfo = LocalWindowInfo.current
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
