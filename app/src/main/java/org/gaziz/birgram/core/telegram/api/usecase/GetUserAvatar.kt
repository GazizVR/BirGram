package org.gaziz.birgram.core.telegram.api.usecase

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.Avatar
import org.gaziz.birgram.core.telegram.api.model.FileData
import org.gaziz.birgram.core.telegram.api.model.ProfilePhoto
import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.ui.icon.skull
import javax.inject.Inject

class GetUserAvatar @Inject constructor(
    private val getAccentColorById: GetAccentColorById,
    private val downloadOrGetFileDataById: DownloadOrGetFileDataById,
    private val userService: UserService
) {
    private fun updateAvatar(
        userId: Long,
        file: FileData
    ) {
        userService.updateUsers { old ->
            val user = old[userId] ?: return@updateUsers old
            var newPhoto = ProfilePhoto(
                small = file,
                miniThumbnail = null
            )
            if(user.photo != null) {
                newPhoto = user.photo.copy(small = file)
            }
            val newUser = user.copy(photo = newPhoto)
            old + (userId to newUser)
        }
    }
    suspend operator fun invoke(
        user: User
    ): Avatar {
        val accentColor = getAccentColorById(user.accentColorId)
            .stateIn(CoroutineScope(Dispatchers.IO))
        val isDeleted = user.type is UserType.Deleted || user.type is UserType.Unknown
        val downloadPhoto = {
            if(user.photo != null) {
                if(user.photo.small.canDownload) {
                    downloadOrGetFileDataById(
                        user.photo.small.id
                    ) { updateAvatar(user.id,it) }
                }
            }
        }
        return when {
            isDeleted -> Avatar.Icon(
                imageVector = skull,
                background = accentColor.value
            )
            user.photo != null && user.photo.small.path.isNotBlank() -> {
                val bitmap = BitmapFactory
                    .decodeFile(user.photo.small.path)
                    .asImageBitmap()
                Avatar.Photo(
                    bitmap = bitmap,
                    onEmpty = downloadPhoto
                )
            }
            user.photo != null && user.photo.miniThumbnail != null -> {
                val miniThumbnail = user.photo.miniThumbnail
                val bitmap = BitmapFactory
                    .decodeByteArray(miniThumbnail,0,miniThumbnail.size)
                    .asImageBitmap()
                Avatar.Photo(
                    bitmap = bitmap,
                    onEmpty = downloadPhoto
                )
            }
            else -> Avatar.PlaceHolder(
                text = if(user.firstName.isNotBlank()) user.firstName[0].toString() else "",
                color = accentColor.value,
                downloadPhoto = {}
            )
        }
    }
}