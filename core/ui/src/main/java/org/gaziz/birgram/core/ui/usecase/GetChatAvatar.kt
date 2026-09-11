package org.gaziz.birgram.core.ui.usecase

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.ui.icon.skull
import org.gaziz.birgram.core.ui.model.Avatar
import org.gaziz.telegram.api.ChatService
import org.gaziz.telegram.api.UserService
import org.gaziz.telegram.api.model.chat.Chat
import org.gaziz.telegram.api.model.chat.ChatType
import org.gaziz.telegram.api.model.media.ProfilePhoto
import org.gaziz.telegram.api.model.media.FileData
import org.gaziz.telegram.api.model.user.UserType
import org.gaziz.telegram.api.usecase.DownloadOrGetFileDataById
import javax.inject.Inject

class GetChatAvatar @Inject constructor(
    private val getAccentColorById: GetAccentColorById,
    private val downloadOrGetFileDataById: DownloadOrGetFileDataById,
    private val chatService: ChatService,
    private val userService: UserService
) {
    private fun updateAvatar(
        chatId: Long,
        file: FileData
    ) {
        chatService.updateChats { old ->
            val chat = old[chatId] ?: return@updateChats old
            var profilePhoto = ProfilePhoto(
                small = file,
                miniThumbnail = null
            )
            chat.photo?.let { photo ->
                profilePhoto = photo.copy(small = file)
            }
            val newChat = chat.copy(photo = profilePhoto)
            old + (chatId to newChat)
        }
    }
    suspend operator fun invoke(
        chat: Chat,
    ): Avatar {
        val accentColor = getAccentColorById(chat.accentColorId)
            .stateIn(CoroutineScope(Dispatchers.IO))
        val placeHolderText = if(chat.title.isNotBlank()) chat.title[0].toString() else ""
        val downloadPhoto: () -> Unit = {
            chat.photo?.let { photo ->
                if(photo.small.canDownload) {
                    downloadOrGetFileDataById(
                        photo.small.id
                    ) { updateAvatar(chat.id,it) }
                }
            }
        }
        val isDeleted =
            chat.type is ChatType.Private &&
            userService.users.value[(chat.type as ChatType.Private).userId]?.type is UserType.Deleted ||
            chat.type is ChatType.Private &&
            userService.users.value[(chat.type as ChatType.Private).userId]?.type is UserType.Unknown
        return when {
            isDeleted -> Avatar.Icon(
                imageVector = skull,
                background = accentColor.value
            )
            chat.photo != null && chat.photo?.small?.path?.isNotBlank() == true -> {
                val bitmap = BitmapFactory
                    .decodeFile(chat.photo!!.small.path)
                    .asImageBitmap()
                Avatar.Photo(
                    bitmap = bitmap,
                    onEmpty = downloadPhoto
                )
            }
            chat.photo != null && chat.photo?.miniThumbnail != null -> {
                val miniThumbnail = chat.photo!!.miniThumbnail!!
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