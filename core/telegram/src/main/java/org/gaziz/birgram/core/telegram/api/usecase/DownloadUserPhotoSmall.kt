package org.gaziz.birgram.core.telegram.api.usecase

import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.media.ProfilePhoto
import javax.inject.Inject

class DownloadUserPhotoSmall @Inject constructor(
    private val downloadOrGetFileDataById: DownloadOrGetFileDataById,
    private val userService: UserService
) {
    operator fun invoke(
        fileId: Int,
        userId: Long
    ) {
        downloadOrGetFileDataById(
            fileId = fileId,
            onFile = { file ->
                userService.updateUsers { old ->
                    val user = old[userId] ?: return@updateUsers old
                    var newPhoto = ProfilePhoto(
                        miniThumbnail = null,
                        small = file
                    )
                    user.photo?.let { photo -> newPhoto = photo.copy(small = file) }
                    old + (userId to user.copy(photo = newPhoto))
                }
            }
        )
    }
}