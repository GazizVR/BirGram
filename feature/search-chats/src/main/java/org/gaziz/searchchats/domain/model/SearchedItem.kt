package org.gaziz.searchchats.domain.model

import org.gaziz.birgram.core.ui.model.AvatarUiState
import org.gaziz.birgram.core.ui.model.ChatTypeUiState

data class SearchedItem(
    val title: String,
    val avatar: AvatarUiState,
    val typeInfo: ChatTypeUiState?
)