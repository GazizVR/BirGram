package org.gaziz.birgram.core.telegram.ui.usecase

import androidx.compose.ui.graphics.Color
import org.gaziz.birgram.core.telegram.api.ChatService
import javax.inject.Inject

class GetAccentColorById @Inject constructor(
    private val chatService: ChatService
) {
    operator fun invoke(id: Int): Color {
        val map = chatService.accentColors.value
        val accentColorId = if(map[id] != null) map[id]?.builtInAccentColorId else id
        return when (accentColorId) {
            0 -> Color(0xFFF44336)
            1 -> Color(0xFFFF9800)
            2 -> Color(0xFF9C27B0)
            3 -> Color(0xFF4CAF50)
            4 -> Color(0xFF00BCD4)
            5 -> Color(0xFF2196F3)
            6 -> Color(0xFFE91E63)
            else -> Color.Blue
        }
    }
}