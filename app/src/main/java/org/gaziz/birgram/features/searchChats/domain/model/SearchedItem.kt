package org.gaziz.birgram.features.searchChats.domain.model

import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.ui.model.ChatAvatar

data class SearchedItem(
    val chat: Chat,
    val avatar: ChatAvatar
)