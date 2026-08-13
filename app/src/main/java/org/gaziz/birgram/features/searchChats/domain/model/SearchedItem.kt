package org.gaziz.birgram.features.searchChats.domain.model

import org.gaziz.birgram.core.ui.model.Avatar
import org.gaziz.birgram.core.ui.model.ChatTypeInfo

data class SearchedItem(
    val title: String,
    val avatar: Avatar,
    val typeInfo: ChatTypeInfo?
)