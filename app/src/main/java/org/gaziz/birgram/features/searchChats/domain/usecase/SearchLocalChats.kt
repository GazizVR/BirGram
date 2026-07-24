package org.gaziz.birgram.features.searchChats.domain.usecase

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.usecase.DownloadChatPhotoSmall
import org.gaziz.birgram.core.telegram.api.usecase.GetAccentColorById
import org.gaziz.birgram.core.ui.model.ChatAvatar
import org.gaziz.birgram.features.searchChats.domain.model.SearchedItem
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import javax.inject.Inject

class SearchLocalChats @Inject constructor(
    private val chatService: ChatService,
    private val chatSearchRepository: ChatSearchRepository,
    private val downloadChatPhotoSmall: DownloadChatPhotoSmall,
    private val getAccentColorById: GetAccentColorById
) {
    operator fun invoke(
        query: String,
        limit: Int
    ) {
        chatService.searchChatsLocal(
            query,
            limit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                val result = it.mapValues { e ->
                    val chat = e.value
                    val accentColor = getAccentColorById(chat.accentColorId).stateIn(
                        CoroutineScope(Dispatchers.IO)
                    )
                    SearchedItem(
                        chat = chat,
                        avatar = when {
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
                        }
                    )
                }
                chatSearchRepository.replace(result)
            }
        }
    }
}