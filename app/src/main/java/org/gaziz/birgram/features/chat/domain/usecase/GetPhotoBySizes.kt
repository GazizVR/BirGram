package org.gaziz.birgram.features.chat.domain.usecase

import org.gaziz.telegram.api.model.media.PhotoSize
import javax.inject.Inject

class GetPhotoBySizes @Inject constructor() {
    operator fun invoke(
        sizes: List<PhotoSize>
    ): PhotoSize? {
        var photo: PhotoSize? = null
        sizes.forEach { size ->
            when(size.type) {
                "y" -> photo = size
                "x" -> photo = size
                "m" -> photo = size
            }
        }
        return photo
    }
}