package org.gaziz.birgram.core.telegram.api.model.chat

import org.gaziz.birgram.core.telegram.api.model.FileData

data class ChatPhoto(
    val miniThumbnail: ByteArray?,
    val small: FileData
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatPhoto

        if (!miniThumbnail.contentEquals(other.miniThumbnail)) return false
        if (small != other.small) return false

        return true
    }

    override fun hashCode(): Int {
        var result = miniThumbnail?.contentHashCode() ?: 0
        result = 31 * result + small.hashCode()
        return result
    }
}