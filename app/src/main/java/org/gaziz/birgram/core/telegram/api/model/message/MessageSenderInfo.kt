package org.gaziz.birgram.core.telegram.api.model.message

import androidx.compose.ui.graphics.Color
import org.gaziz.birgram.core.telegram.api.model.Avatar

data class MessageSenderInfo(
    val name: String,
    val avatar: Avatar? = null,
    val accentColor: Color
)