package org.gaziz.birgram.features.chatList.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import java.time.LocalDateTime

data class LastMsgUiState (
    val component: @Composable (Modifier) -> Unit,
    val date: LocalDateTime,
    val fontSize: TextUnit
)