package org.gaziz.birgram.features.chatList.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit

data class LastMsgUiState (
    val component: @Composable (Modifier) -> Unit = {},
    val date: String,
    val fontSize: TextUnit
)