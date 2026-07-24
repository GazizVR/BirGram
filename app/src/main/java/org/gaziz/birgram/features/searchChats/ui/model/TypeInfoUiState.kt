package org.gaziz.birgram.features.searchChats.ui.model

import androidx.compose.ui.unit.TextUnit
import org.gaziz.birgram.features.searchChats.domain.model.ChatTypeInfo

data class TypeInfoUiState(
    val info: ChatTypeInfo,
    val fontSize: TextUnit
)
