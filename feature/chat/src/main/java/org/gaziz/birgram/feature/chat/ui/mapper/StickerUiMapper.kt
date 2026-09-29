package org.gaziz.birgram.feature.chat.ui.mapper

import org.gaziz.birgram.core.telegram.api.model.StickerFormat
import org.gaziz.birgram.core.telegram.api.model.message.MessageContent
import org.gaziz.birgram.feature.chat.ui.model.StickerUiState
import java.io.File
import javax.inject.Inject

class StickerUiMapper @Inject constructor() {
    fun map(
        sticker: MessageContent.Sticker,
        downloadMedia: (Int) -> Unit
    ): StickerUiState {
        return if(sticker.data.path.isNotBlank()) {
            when(sticker.format){
                StickerFormat.Tgs -> StickerUiState.Animation(sticker.data.path)
                StickerFormat.WebM -> StickerUiState.Video(File(sticker.data.path))
                StickerFormat.WebP -> StickerUiState.Picture(File(sticker.data.path))
            }
        } else {
            StickerUiState.Empty(
                sticker.emoji
            ) {
                if(sticker.data.canDownload) {
                    downloadMedia(sticker.data.id)
                }
            }
        }
    }
}