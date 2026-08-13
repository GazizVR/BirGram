package org.gaziz.birgram.core.ui.usecase

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.Chat
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.media.ChatPhoto
import org.gaziz.birgram.core.telegram.api.model.media.FileData
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.api.usecase.DownloadOrGetFileDataById
import org.gaziz.birgram.core.ui.icon.skull
import org.gaziz.birgram.core.ui.model.Avatar
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
            var chatPhoto = ChatPhoto(
                small = file,
                miniThumbnail = null
            )
            if(chat.photo != null) {
                chatPhoto = chat.photo.copy(small = file)
            }
            val newChat = chat.copy(photo = chatPhoto)
            old + (chatId to newChat)
        }
    }
    suspend operator fun invoke(
        chat: Chat,
    ): Avatar {
        val accentColor = getAccentColorById(chat.accentColorId)
            .stateIn(CoroutineScope(Dispatchers.IO))
        val placeHolderText = if(chat.title.isNotBlank()) chat.title[0].toString() else ""
        val downloadPhoto = {
            if(chat.photo != null) {
                if(chat.photo.small.canDownload) {
                    downloadOrGetFileDataById(
                        chat.photo.small.id
                    ) { updateAvatar(chat.id,it) }
                }
            }
        }
        val isDeleted =
            chat.type is ChatType.Private &&
            userService.users.value[chat.type.userId]?.type is UserType.Deleted ||
            chat.type is ChatType.Private &&
            userService.users.value[chat.type.userId]?.type is UserType.Unknown
        return when {
            isDeleted -> Avatar.Icon(
                imageVector = skull,
                background = accentColor.value
            )
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