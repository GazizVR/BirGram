package org.gaziz.birgram.features.searchChats.domain.model

import org.gaziz.birgram.core.telegram.api.model.media.Avatar
import org.gaziz.birgram.core.ui.model.ChatTypeInfo

data class SearchedItem(
    val title: String,
    val avatar: Avatar,
    val typeInfo: ChatTypeInfo?
)