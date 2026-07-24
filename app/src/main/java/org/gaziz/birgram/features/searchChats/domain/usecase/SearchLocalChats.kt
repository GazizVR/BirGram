package org.gaziz.birgram.features.searchChats.domain.usecase

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.api.usecase.DownloadChatPhotoSmall
import org.gaziz.birgram.core.telegram.api.usecase.GetAccentColorById
import org.gaziz.birgram.core.ui.model.ChatAvatar
import org.gaziz.birgram.features.searchChats.domain.model.ChatTypeInfo
import org.gaziz.birgram.features.searchChats.domain.model.SearchedItem
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import javax.inject.Inject

class SearchLocalChats @Inject constructor(
    private val chatService: ChatService,
    private val chatSearchRepository: ChatSearchRepository,
    private val downloadChatPhotoSmall: DownloadChatPhotoSmall,
    private val getAccentColorById: GetAccentColorById,
    private val userService: UserService,
    private val groupService: GroupService
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
                    val typeInfo: ChatTypeInfo? = when(val type = chat.type) {
                        is ChatType.BasicGroup -> {
                            val group = groupService.basicGroups.value[type.groupId]
                            if(group != null) {
                                ChatTypeInfo.BasicGroup(
                                    memberCount = group.memberCount,
                                )
                            } else {
                                null
                            }
                        }
                        is ChatType.Private -> {
                            val user = userService.users.value[type.userId]
                            if(user != null) {
                                ChatTypeInfo.User(
                                    status = user.status,
                                    isBot = user.type is UserType.Bot
                                )
                            } else {
                                null
                            }
                        }
                        is ChatType.Secret -> {
                            val user = userService.users.value[type.userId]
                            if(user != null) {
                                ChatTypeInfo.User(
                                    status = user.status,
                                    isBot = user.type is UserType.Bot
                                )
                            } else {
                                null
                            }
                        }
                        is ChatType.SuperGroup -> {
                            val group = groupService.superGroups.value[type.groupId]
                            if(group != null) {
                                ChatTypeInfo.SuperGroup(
                                    memberCount = group.memberCount,
                                    isChannel = type.isChannel
                                )
                            } else {
                                null
                            }
                        }
                        else -> null
                    }
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
                        },
                        typeInfo = typeInfo
                    )
                }
                chatSearchRepository.replace(result)
            }
        }
    }
}