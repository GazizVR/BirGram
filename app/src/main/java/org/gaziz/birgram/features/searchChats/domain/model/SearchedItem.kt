package org.gaziz.birgram.features.searchChats.domain.model

import org.gaziz.birgram.core.ui.model.ChatAvatar
import org.gaziz.birgram.core.ui.model.ChatTypeInfo

data class SearchedItem(
    val title: String,
    val avatar: ChatAvatar,
    val typeInfo: ChatTypeInfo?
)