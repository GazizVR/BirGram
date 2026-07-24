package org.gaziz.birgram.features.chat.domain.usecase

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.api.usecase.DownloadChatPhotoSmall
import org.gaziz.birgram.core.telegram.api.usecase.GetAccentColorById
import org.gaziz.birgram.core.ui.icon.skull
import org.gaziz.birgram.core.ui.model.ChatAvatar
import org.gaziz.birgram.features.chat.domain.model.ChatInfo
import javax.inject.Inject

class GetChatById @Inject constructor(
    private val chatService: ChatService,
    private val downloadChatPhotoSmall: DownloadChatPhotoSmall,
    private val getAccentColorById: GetAccentColorById,
    private val userService: UserService
) {
    operator fun invoke(
        id: Long
    ): Flow<ChatInfo?> {
        return chatService.chats.map {
            val chat = it[id] ?: return@map null
            val accentColor = getAccentColorById(chat.accentColorId).stateIn(
                CoroutineScope(Dispatchers.IO)
            )
            val isDeleted = (
                chat.type is ChatType.Private &&
                userService.users.value[chat.type.userId]?.type !is UserType.Regular &&
                userService.users.value[chat.type.userId]?.type !is UserType.Bot
            )
            ChatInfo(
                title = chat.title,
                isDeleted = isDeleted,
                avatar = when {
                    isDeleted -> {
                        ChatAvatar.Icon(
                            imageVector = skull,
                            background = accentColor.value
                        )
                    }
                    chat.photo != null && chat.photo.small.path.isNotBlank() -> {
                        val bitmap = BitmapFactory.decodeFile(chat.photo.small.path)
                        val image = bitmap.asImageBitmap()
                        ChatAvatar.Photo(image)
                    }
                    chat.photo != null && chat.photo.miniThumbnail != null -> {
                        ChatAvatar.Photo(
                            bitmap = chat.photo.miniThumbnail.decodeToImageBitmap(),
                            onEmpty = {
                                downloadChatPhotoSmall(chat.id,chat.photo.small.id)
                            }
                        )
                    }
                    else -> ChatAvatar.PlaceHolder(
                        text = if(chat.title.isNotBlank()) chat.title[0].toString() else "",
                        color = accentColor.value,
                        downloadPhoto = {
                            if(chat.photo != null) {
                                downloadChatPhotoSmall(chat.id,chat.photo.small.id)
                            }
                        }
                    )
                },

                )
        }
    }
}