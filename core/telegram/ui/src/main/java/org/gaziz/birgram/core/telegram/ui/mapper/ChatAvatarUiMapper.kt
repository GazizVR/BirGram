package org.gaziz.birgram.core.telegram.ui.mapper

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.ui.model.AvatarUiState
import org.gaziz.birgram.core.ui.icon.skull
import javax.inject.Inject

class ChatAvatarUiMapper @Inject constructor(
    private val accentColorMapper: AccentColorMapper
) {
    operator fun invoke(
        chat: Chat,
        chatsById: Map<Long, Chat>,
        usersById: Map<Long, User>,
        onDownload: (Int) -> Unit
    ): AvatarUiState {
        val accentColor = accentColorMapper(chat.accentColorId)
        val placeHolderText = if(chat.title.isNotBlank()) chat.title[0].toString() else ""
        val isDeleted = run {
            val type = chat.type
            if(type is ChatType.Private) {
                usersById[type.userId]?.type is UserType.Deleted ||
                usersById[type.userId]?.type is UserType.Unknown
            } else {
                false
            }
        }
        val photo = chat.photo
        return when {
            isDeleted -> AvatarUiState.Icon(
                imageVector = skull,
                background = accentColor
            )
            photo != null && photo.small.path.isNotBlank() -> {
                val path = photo.small.path
                AvatarUiState.Photo(path)
            }
            photo != null && photo.miniThumbnail != null -> {
                val miniThumbnail = photo.miniThumbnail!!
                val bitmap = BitmapFactory
                    .decodeByteArray(miniThumbnail,0,miniThumbnail.size)
                    .asImageBitmap()
                AvatarUiState.Thumbnail(
                    bitmap = bitmap,
                    onDownload = { onDownload(photo.small.id) }
                )
            }
            photo != null -> AvatarUiState.PlaceHolder(
                text = placeHolderText,
                color = accentColor,
                onDownload = { onDownload(photo.small.id) }
            )
            else -> AvatarUiState.PlaceHolder(
                text = placeHolderText,
                color = accentColor,
                onDownload = {}
            )
        }
    }
}