package org.gaziz.birgram.core.telegram.api.usecase

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.Avatar
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.ui.icon.skull
import javax.inject.Inject

class GetChatAvatar @Inject constructor(
    private val getAccentColorById: GetAccentColorById,
    private val downloadChatPhotoSmall: DownloadChatPhotoSmall,
    private val userService: UserService
) {
    suspend operator fun invoke(
        chat: Chat,
    ): Avatar {
        val accentColor = getAccentColorById(chat.accentColorId)
            .stateIn(CoroutineScope(Dispatchers.IO))
        val placeHolderText = if(chat.title.isNotBlank()) chat.title[0].toString() else ""
        val downloadPhoto = {
            if(chat.photo != null) {
                if(chat.photo.small.canDownload) {
                    downloadChatPhotoSmall(chat.id,chat.photo.small.id)
                }
            }
        }
        val isDeleted =
            chat.type is ChatType.Private &&
                    userService.users.value[chat.type.userId]?.type == UserType.Deleted &&
                    userService.users.value[chat.type.userId]?.type == UserType.Unknown
        return when {
            isDeleted -> {
                Avatar.Icon(
                    imageVector = skull,
                    background = accentColor.value
                )
            }
            chat.photo != null && chat.photo.small.path.isNotBlank() -> {
                val bitmap = BitmapFactory
                    .decodeFile(chat.photo.small.path)
                    .asImageBitmap()
                Avatar.Photo(
                    bitmap = bitmap,
                    onEmpty = downloadPhoto
                )
            }
            chat.photo != null && chat.photo.miniThumbnail != null -> {
                val miniThumbnail = chat.photo.miniThumbnail
                val bitmap = BitmapFactory
                    .decodeByteArray(miniThumbnail,0,miniThumbnail.size)
                    .asImageBitmap()
                Avatar.Photo(
                    bitmap = bitmap,
                    onEmpty = downloadPhoto
                )
            }
            else -> Avatar.PlaceHolder(
                text = placeHolderText,
                color = accentColor.value,
                downloadPhoto = downloadPhoto
            )
        }
    }
}