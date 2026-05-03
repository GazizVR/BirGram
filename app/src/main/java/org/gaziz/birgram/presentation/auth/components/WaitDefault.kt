package org.gaziz.birgram.presentation.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WaitDefault() {
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background))
}
