package org.gaziz.birgram.features.searchChats.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gaziz.birgram.core.telegram.api.ChatService
import org.gaziz.birgram.core.telegram.api.GroupService
import org.gaziz.birgram.core.telegram.api.UserService
import org.gaziz.birgram.core.telegram.api.model.chat.ChatType
import org.gaziz.birgram.core.telegram.api.model.user.UserType
import org.gaziz.birgram.core.telegram.api.usecase.GetChatAvatar
import org.gaziz.birgram.core.ui.model.ChatTypeInfo
import org.gaziz.birgram.features.searchChats.domain.model.SearchedItem
import org.gaziz.birgram.features.searchChats.domain.repository.ChatSearchRepository
import javax.inject.Inject

class SearchLocalChats @Inject constructor(
    private val chatService: ChatService,
    private val chatSearchRepository: ChatSearchRepository,
    private val userService: UserService,
    private val groupService: GroupService,
    private val getChatAvatar: GetChatAvatar
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
                    val avatar = getChatAvatar(chat)
                    SearchedItem(
                        title = chat.title,
                        avatar = avatar,
                        typeInfo = typeInfo
                    )
                }
                chatSearchRepository.replace(result)
            }
        }
    }
}