package org.gaziz.birgram.core.telegram.api.model.message

import org.gaziz.birgram.core.telegram.api.model.Avatar

data class MessageSenderInfo(
    val name: String,
    val avatar: Avatar
)