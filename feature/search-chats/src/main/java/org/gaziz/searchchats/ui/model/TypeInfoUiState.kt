package org.gaziz.searchchats.ui.model

import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.core.ui.model.ChatTypeUiState

data class TypeInfoUiState(
    val info: ChatTypeUiState,
    val fontSize: TextUnit
)
