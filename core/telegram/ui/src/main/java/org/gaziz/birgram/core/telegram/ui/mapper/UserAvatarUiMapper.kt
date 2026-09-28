package org.gaziz.birgram.core.telegram.ui.mapper

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.ui.model.AvatarUiState
import org.gaziz.birgram.core.ui.icon.skull
import javax.inject.Inject

class UserAvatarUiMapper @Inject constructor(
    private val accentColorMapper: AccentColorMapper
) {
    operator fun invoke(
        user: User,
        onDownload: (Int) -> Unit
    ): AvatarUiState {
        val accentColor = accentColorMapper(user.accentColorId)
        val isDeleted = user.type is UserType.Deleted || user.type is UserType.Unknown
        val photo = user.photo
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
                text = if(user.firstName.isNotBlank()) user.firstName[0].toString() else "",
                color = accentColor,
                onDownload = { onDownload(photo.small.id) }
            )
            else -> AvatarUiState.PlaceHolder(
                text = if(user.firstName.isNotBlank()) user.firstName[0].toString() else "",
                color = accentColor,
                onDownload = {}
            )
        }
    }
}