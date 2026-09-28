package org.gaziz.birgram.core.telegram.ui.provider

import org.gaziz.birgram.core.telegram.api.model.user.User
import org.gaziz.birgram.core.telegram.api.usecase.DownloadUserPhotoSmall
import org.gaziz.birgram.core.telegram.ui.mapper.UserAvatarUiMapper
import org.gaziz.birgram.core.telegram.ui.model.AvatarUiState
import javax.inject.Inject

class UserAvatarProvider @Inject constructor(
    private val userAvatarUiMapper: UserAvatarUiMapper,
    private val downloadUserPhotoSmall: DownloadUserPhotoSmall
) {
    operator fun invoke(user: User): AvatarUiState {
        return userAvatarUiMapper(
            user = user,
            onDownload = { downloadUserPhotoSmall(it,user.id) }
        )
    }
}